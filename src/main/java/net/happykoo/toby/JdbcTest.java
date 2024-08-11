package net.happykoo.toby;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JdbcTest {
    public static void main(String[] args) throws ClassNotFoundException {
        BeanDAO beanDAO = new BeanDAO();

//        beanDAO.add(new BeanVO("TEST"));
        beanDAO.update(new BeanVO(7, "TEST3"));

        List<BeanVO> beanList = beanDAO.findAll();

        for (BeanVO beanVO : beanList) {
            System.out.println(beanVO.toString());
        }
    }
}

class BeanDAO {
    public List<BeanVO> findAll() {
        try(Connection connection = getConnection();
            PreparedStatement psmt = connection.prepareStatement("SELECT ID, NAME FROM BEAN");
            ResultSet rs = psmt.executeQuery()) {
            List<BeanVO> beanList = new ArrayList<>();
            while (rs.next()) {
                beanList.add(new BeanVO(rs.getInt("ID"), rs.getString("NAME")));
            }

            return beanList;
        } catch (SQLException e) {
            //에러로그 DB
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void add(BeanVO beanVO) {
        try(Connection connection = getConnection();
            PreparedStatement psmt = connection.prepareStatement("INSERT INTO BEAN(ID, NAME) VALUES(BEAN_SEQ.nextval, ?)")) {
            psmt.setString(1, beanVO.getName());
            psmt.executeUpdate();
        } catch (SQLException e) {
            //에러로그 DB
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void update(BeanVO beanVO) {
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(true);
            try (PreparedStatement psmt = connection.prepareStatement("UPDATE BEAN SET NAME = ? WHERE ID = ?")) {
                psmt.setString(1, beanVO.getName());
                psmt.setInt(2, beanVO.getId());
                psmt.executeUpdate();
            } catch (SQLException e) {
                throw e;
            }
        } catch (SQLException e) {

        } finally {
            if (Objects.nonNull(connection)) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void delete(BeanVO beanVO) {

    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            return DriverManager.getConnection("jdbc:oracle:thin:@129.154.220.177:1521:XE", "hr", "bean1234");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

class BeanVO {
    private int id;
    private String name;

    public BeanVO(String name) {
        this.name = name;
    }

    public BeanVO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ID : ");
        sb.append(id);
        sb.append(", NAME: ");
        sb.append(name);

        return sb.toString();
    }
}

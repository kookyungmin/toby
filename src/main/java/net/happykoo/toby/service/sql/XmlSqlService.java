package net.happykoo.toby.service.sql;

public class XmlSqlService extends BaseSqlService {
    public XmlSqlService(String filePath) {
        super(new JaxbSqlLoader(filePath), new HashMapSqlRegistry());
    }
}

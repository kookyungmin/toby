package net.happykoo.toby.dto;

import jakarta.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {
    public SqlMap createSqlMap() {
        return new SqlMap();
    }
}

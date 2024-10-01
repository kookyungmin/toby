package net.happykoo.toby.dto;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@XmlRootElement(name = "sqlmap")
@XmlAccessorType(XmlAccessType.FIELD)
public class SqlMap {
    @XmlElement(required = true)
    private List<SqlType> sql = new ArrayList<>();
}

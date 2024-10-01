package net.happykoo.toby.dto;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class SqlType {
    @XmlValue
    private String value;
    @XmlAttribute(required = true)
    private String key;
}

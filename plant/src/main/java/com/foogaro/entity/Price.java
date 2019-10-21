package com.foogaro.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Price implements Serializable {

    @Id
    private long id;
    private Product product;
    private Date validFrom;
    private Date validUntil;
    private long price;

}

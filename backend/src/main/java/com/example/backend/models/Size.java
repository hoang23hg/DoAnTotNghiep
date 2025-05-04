package com.example.backend.models;

import com.example.backend.converter.SizeNameConverter;
import jakarta.persistence.*;

@Entity
@Table(name = "size")
public class Size {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "size_id")
    private Integer sizeId;

    @Convert(converter = SizeNameConverter.class)
    @Column(name = "size_name", nullable = false)
    private SizeName sizeName;

    public Size() {}

    public Size(Integer sizeId, SizeName sizeName) {
        this.sizeId = sizeId;
        this.sizeName = sizeName;
    }

    public Integer getSizeId() {
        return sizeId;
    }

    public void setSizeId(Integer sizeId) {
        this.sizeId = sizeId;
    }

    public SizeName getSizeName() {
        return sizeName;
    }

    public void setSizeName(SizeName sizeName) {
        this.sizeName = sizeName;
    }
}

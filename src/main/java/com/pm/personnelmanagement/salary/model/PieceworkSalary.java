package com.pm.personnelmanagement.salary.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "salaries")
public class PieceworkSalary extends Salary {
    private List<Piece> pieces;
}

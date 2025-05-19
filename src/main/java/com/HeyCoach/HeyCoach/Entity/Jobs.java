package com.HeyCoach.HeyCoach.Entity;

import lombok.Data;

import java.util.ArrayList;
@Data
public class Jobs {
    private String title;
    private String location;
    private ArrayList<String> companyName;
    private ArrayList<String> companyId;
    private String publishedAt;
    private int rows;
}

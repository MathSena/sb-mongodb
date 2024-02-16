package com.mathsena.sbmongodb.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "players")
@Data
public class Player {

    private int id;
    private String name;
    private String position;
    private int jerseyNumber;
    private String teamName;


}

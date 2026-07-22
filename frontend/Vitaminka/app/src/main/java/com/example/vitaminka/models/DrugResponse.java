package com.example.vitaminka.models;

public class DrugResponse {
    public int id;
    public String name;
    public String category;
    public String inn;
    public String dose;
    public Manufacturer manufacturer;
    public Type type;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getInn() {
        return inn;
    }

    public String getDose() {
        return dose;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public Type getType() {
        return type;
    }

    public static class Manufacturer {
        public int id;
        public String name;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public static class Type {
        public int id;
        public String name;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
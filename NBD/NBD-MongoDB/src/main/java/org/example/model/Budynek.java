package org.example.model;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class Budynek {

    private ObjectId _id;

    private String nazwa;

    private List<Lokal> lokale;

    public Budynek(String nazwa) {
        this._id = new ObjectId();
        this.nazwa = nazwa;
        this.lokale = new ArrayList<>();
    }

    public Budynek(ObjectId _id,String nazwa) {
        this._id = _id;
        this.nazwa = nazwa;
        this.lokale = new ArrayList<>();
    }

    public Budynek() {

    }

    public ObjectId getId() {
        return _id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public List<Lokal> getLokale() {
        return lokale;
    }

    public void setLokale(List<Lokal> lokale) {
        this.lokale = lokale;
    }

    public void dodajLokal(Lokal lokal) {
        if (lokal != null) {
            lokal.setBudynek(this);
            lokale.add(lokal);
        }
    }

    public double czynszCalkowity() {
        double suma = 0;
        for (Lokal lokal : lokale) {
            suma += lokal.czynsz();
        }
        return suma;
    }

    public double zysk(double wydatkiZaMetr) {
        double suma = 0;
        for (Lokal lokal : lokale) {
            suma += lokal.czynsz() - (lokal.dajPowierzchnie() * wydatkiZaMetr);
        }
        return Math.max(suma, 0);
    }

    public String informacjaZbiorcza() {
        StringBuilder info = new StringBuilder();
        for (Lokal lokal : lokale) {
            info.append(lokal.informacja()).append("\n");
        }
        return info.toString();
    }
}

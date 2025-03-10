package org.example.mappers;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.model.Budynek;
import org.example.model.Lokal;

import java.util.List;
import java.util.stream.Collectors;

public class BudynekMapper {

    private LokalMapper lokalMapper;

    public LokalMapper getLokalMapper() {
        return lokalMapper;
    }

    public BudynekMapper(LokalMapper lokalMapper) {
        this.lokalMapper = lokalMapper;
    }

    public void setLokalMapper(LokalMapper lokalMapper) {
        this.lokalMapper = lokalMapper;
    }

    // Metoda konwertująca Budynek na Document
    public Document toDocument(Budynek budynek) {
        Document document = new Document();

        document.put("_id", budynek.getId());
        document.put("nazwa", budynek.getNazwa());

        // Konwersja listy obiektów Lokal do listy dokumentów BSON
        List<Document> lokaleDocuments = budynek.getLokale().stream()
                .map(lokalMapper::toDocument)  // Użycie lokalMapper do konwersji każdego Lokal
                .collect(Collectors.toList());

        document.put("lokale", lokaleDocuments);

        return document;
    }

    // Metoda konwertująca Document na Budynek
    public Budynek fromDocument(Document document) {
        ObjectId _id = (ObjectId) document.get("_id");
        String nazwa = document.getString("nazwa");

        Budynek budynek = new Budynek(_id, nazwa);

        // Pobranie listy dokumentów "lokale" i konwersja na listę obiektów Lokal
        List<Document> lokaleDocs = document.getList("lokale", Document.class);
        if (lokaleDocs != null) {
            List<Lokal> lokale = lokaleDocs.stream()
                    .map(lokalMapper::fromDocument)  // Konwertuje każdy dokument na obiekt Lokal
                    .collect(Collectors.toList());
            budynek.setLokale(lokale);  // Ustawienie listy lokali w obiekcie Budynek
        }

        return budynek;
    }

}

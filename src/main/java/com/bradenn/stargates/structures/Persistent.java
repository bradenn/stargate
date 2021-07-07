package com.bradenn.stargates.structures;

import com.bradenn.stargates.Database;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.Objects;
import java.util.UUID;

public interface Persistent {

    default void rebuild() {
        destroy();
        build();
    }

    void build();

    void destroy();

    Document getDocument();

    default String getIdentifier() {
        return "unknown";
    }

    UUID getUUID();

    default void save() {
        MongoCollection<Document> collection = Database.getCollection(getIdentifier());
        Document uuidDocument = new Document("uuid", getUUID().toString());
        if (Objects.isNull(collection.find(uuidDocument).first())) {
            collection.insertOne(getDocument());
        } else {
            collection.findOneAndReplace(uuidDocument, getDocument());
        }
    }

}

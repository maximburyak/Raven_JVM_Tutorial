package net.ravendb.demo.db;

import net.ravendb.client.documents.DocumentStore;
import net.ravendb.client.documents.IDocumentStore;

// todo: please implement eager intialization of singleton, like in: https://ravendb.net/docs/article-page/4.1/java/client-api/creating-document-store#singleton
public enum RavenDBDocumentStore {
INSTANCE;
	
	private static IDocumentStore store;

    static {
        store = new DocumentStore("http://127.0.0.1:18080", // todo: use the overload that provides array of url strings

                "Hospital");

    }

    public IDocumentStore getStore() {
    	store.initialize(); // todo: call store.initialize right after we create the store, it's a singletone anyway
        return store;
    }
}

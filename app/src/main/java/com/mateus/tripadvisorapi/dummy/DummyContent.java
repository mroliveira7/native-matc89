package com.mateus.tripadvisorapi.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        //for (int i = 1; i <= COUNT; i++) {
            addItem(new DummyItem("1", "Alan Atta", makeDetails("Alan",1)));
            addItem(new DummyItem("2", "Lucas Almeida", makeDetails("Lucas",2)));
            addItem(new DummyItem("3", "Marcos Ricardo", makeDetails("Marcos",3)));
            addItem(new DummyItem("4", "Mateus Barreto", makeDetails("Mateus",4)));

        //}

    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails("eoq",position));
    }

    private static String makeDetails(String name, int position) {
        StringBuilder builder = new StringBuilder();
        //builder.append("Details about "+name+": ").append(position);
        //DESCRICAO GERAL
        builder.append("\nAluno de Ciência da Computação na Unversidade Federal da Bahia");
            if (name == "Alan"){
                //DESCRICAO ALAN
                //builder.append("\nAluno de Ciência da Computação na Unversidade Federal da Bahia");
            }
            else if (name == "Lucas"){
                //DESCRICAO Lucas
                // builder.append("\nAluno de Ciência da Computação na Unversidade Federal da Bahia");
            }
            else if (name == "Marcos"){
                //DESCRICAO MARCOS
                //builder.append("\nAluno de Ciência da Computação na Unversidade Federal da Bahia");
            }
            else if (name == "Mateus"){
                //DESCRICAO MATEUS
                //builder.append("\nAluno de Ciência da Computação na Unversidade Federal da Bahia");
            }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}

package de.jandrotek.android.aspectra.viewer;


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
public class ListDummyContent {

//    /**
//     * An array of sample (dummy) items.
//     */
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * An array of spectra items.
     */

//    public static List<SpectrumItem> ITEMS = new ArrayList<SpectrumItem>();


//    /**
//     * A map of sample (dummy) items, by ID.
//     */
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
//    public static Map<String, SpectrumItem> ITEM_MAP = new HashMap<String, SpectrumItem>();

//    static {
//    	if(SpectraFiles.mFilelNameListOutput.length > 0){
//    	for(int i = 0; i < SpectraFiles.mFilelNameListOutput.length; i++)
//        // Add 3 sample items.
//    		addItem(new DummyItem(""+i, SpectraFiles.mFilelNameListOutput[i]));
//    	}
//    	else
//    	{
//    		addItem(new DummyItem("1", "no File 1"));
//    		addItem(new DummyItem("2", "no File 2"));
//    		addItem(new DummyItem("3", "no File 3"));
//
//    	}
//    }

//    static {
//    	if(SpectrumFiles.mFilelNameListOutput.length > 0){
//    	for(int i = 0; i < SpectrumFiles.mFilelNameListOutput.length; i++)
//    		addItem(new SpectrumItem("1", SpectrumFiles.mFilelNameListOutput[i], "some notes"));
//    	}
//    	else
//    	{
//    		addItem(new SpectrumItem("1","Unknown Path", "some notes"));
//    		addItem(new SpectrumItem("2","Unknown Path", "some notes"));
//    		addItem(new SpectrumItem("3","Unknown Path", "some notes"));
//
//    	}
//    }


    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

//    private static void addItem(SpectrumItem item) {
//        ITEMS.add(item);
//        ITEM_MAP.put(item.id, item);
//    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public String id;
//        public String content;
		public String name;
		public String notes;

        public DummyItem(String id, String name, String notes) {
            this.id = id;
			this.name = name;
			this.notes = notes;
		}

		@Override
		public String toString(){
			return name;
		}

		public String getName(){
			return name;
		}

		public String getNotes(){
			return notes;
		}
    }

    public static class SpectrumItem {
    	public String id;
    	public String name;
    	public String notes;

    	public SpectrumItem(String id, String name, String notes){
    		this.id = id;
    		this.name = name;
    		this.notes = notes;
    	}

    	@Override
    	public String toString(){
    		return name;
    	}

    	public String getName(){
    		return name;
    	}

    	public String getNotes(){
    		return notes;
    	}
    }
}

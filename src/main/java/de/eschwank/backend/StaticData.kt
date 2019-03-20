package de.eschwank.backend

import java.util.LinkedHashMap
import java.util.stream.Stream

internal object StaticData {

    private val MINERAL_WATER = "Mineral Water"
    private val SOFT_DRINK = "Soft Drink"
    private val COFFEE = "Coffee"
    private val TEA = "Tea"
    private val DAIRY = "Dairy"
    private val CIDER = "Cider"
    private val BEER = "Beer"
    private val WINE = "Wine"
    private val OTHER = "Other"

    val UNDEFINED = "Undefined"

    val BEVERAGES: MutableMap<String, String> = LinkedHashMap()

    init {
        Stream.of("Evian",
                "Voss",
                "Veen",
                "San Pellegrino",
                "Perrier")
                .forEach { name -> BEVERAGES[name] = MINERAL_WATER }

        Stream.of("Coca-Cola",
                "Fanta",
                "Sprite")
                .forEach { name -> BEVERAGES[name] = SOFT_DRINK }

        Stream.of("Maxwell Ready-to-Drink Coffee",
                "Nescafé Gold",
                "Starbucks East Timor Tatamailau")
                .forEach { name -> BEVERAGES[name] = COFFEE }

        Stream.of("Prince Of Peace Organic White Tea",
                "Pai Mu Tan White Peony Tea",
                "Tazo Zen Green Tea",
                "Dilmah Sencha Green Tea",
                "Twinings Earl Grey",
                "Twinings Lady Grey",
                "Classic Indian Chai")
                .forEach { name -> BEVERAGES[name] = TEA }

        Stream.of("Cow's Milk",
                "Goat's Milk",
                "Unicorn's Milk",
                "Salt Lassi",
                "Mango Lassi",
                "Airag")
                .forEach { name -> BEVERAGES[name] = DAIRY }

        Stream.of("Crowmoor Extra Dry Apple",
                "Golden Cap Perry",
                "Somersby Blueberry",
                "Kopparbergs Naked Apple Cider",
                "Kopparbergs Raspberry",
                "Kingstone Press Wild Berry Flavoured Cider",
                "Crumpton Oaks Apple",
                "Frosty Jack's",
                "Ciderboys Mad Bark",
                "Angry Orchard Stone Dry",
                "Walden Hollow",
                "Fox Barrel Wit Pear")
                .forEach { name -> BEVERAGES[name] = CIDER }

        Stream.of("Budweiser",
                "Miller",
                "Heineken",
                "Holsten Pilsener",
                "Krombacher",
                "Weihenstephaner Hefeweissbier",
                "Ayinger Kellerbier",
                "Guinness Draught",
                "Kilkenny Irish Cream Ale",
                "Hoegaarden White",
                "Barbar",
                "Corsendonk Agnus Dei",
                "Leffe Blonde",
                "Chimay Tripel",
                "Duvel",
                "Pilsner Urquell",
                "Kozel",
                "Staropramen",
                "Lapin Kulta IVA",
                "Kukko Pils III",
                "Finlandia Sahti")
                .forEach { name -> BEVERAGES[name] = BEER }

        Stream.of("Jacob's Creek Classic Shiraz",
                "Chateau d’Yquem Sauternes",
                "Oremus Tokaji Aszú 5 Puttonyos")
                .forEach { name -> BEVERAGES[name] = WINE }

        Stream.of("Pan Galactic Gargle Blaster",
                "Mead",
                "Soma")
                .forEach { name -> BEVERAGES[name] = OTHER }

        BEVERAGES[""] = UNDEFINED
    }
}
/** This class is not meant to be instantiated.  */

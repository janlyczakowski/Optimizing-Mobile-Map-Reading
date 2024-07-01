package com.janlyczakowski.masterthesis

// education => college, university, driving_school, kindergarten, language_school, research_institute, music_school, school
// bank_atm => atm, bank
// doctor_clinic => doctors, clinic
//nightclub_dance => dance, nightclub
// post => post_box, post_office
//  church => place of worship
// lodging => hotel, apartment, guest_house, hostel
//castle_palace => castle
// sport_fitness => fitness_centre, fitness_station, sport_centre, pitch
// swimming => swimming_pool, water_park
// train_station => station
// tram_station => tram_stop
// building/parking => same icon as for amenity/parking

data class Icon(
    val path: String,
    val functions: List<String>
)

class PoiHelper {

    val icons = listOf(
        Icon(path = "R.drawable.bar", functions = listOf("bar")),
        Icon(path = "R.drawable.restaurant", functions = listOf("restaurant")),
        Icon(path = "R.drawable.cafe", functions = listOf("cafe")),
        Icon(path = "R.drawable.fast_food", functions = listOf("fast_food")),
        Icon(path = "R.drawable.ice_cream", functions = listOf("ice_cream")),
        Icon(path = "R.drawable.library", functions = listOf("library")),
        Icon(path = "R.drawable.education", functions = listOf("college", "university", "driving_school", "kindergarten", "language_school", "research_institute", "music_school", "school")),
        Icon(path = "R.drawable.bicycle_rental", functions = listOf("bicycle_rental")),
        Icon(path = "R.drawable.charging_station", functions = listOf("charging_station")),
        Icon(path = "R.drawable.parking", functions = listOf("parking")),
        Icon(path = "R.drawable.bank_atm", functions = listOf("atm", "bank")),
        Icon(path = "R.drawable.doctor_clinic", functions = listOf("doctors", "clinic")),
        Icon(path = "R.drawable.dentist", functions = listOf("dentist")),
        Icon(path = "R.drawable.hospital", functions = listOf("hospital")),
        Icon(path = "R.drawable.pharmacy", functions = listOf("pharmacy")),
        Icon(path = "R.drawable.veterinary", functions = listOf("veterinary")),
        Icon(path = "R.drawable.art_center", functions = listOf("arts_centre")),
        Icon(path = "R.drawable.cinema", functions = listOf("cinema")),
        Icon(path = "R.drawable.fountain", functions = listOf("fountain")),
        Icon(path = "R.drawable.nightclub_dance", functions = listOf("dance", "nightclub")),
        Icon(path = "R.drawable.theatre", functions = listOf("theatre")),
        Icon(path = "R.drawable.police", functions = listOf("police")),
        Icon(path = "R.drawable.post", functions = listOf("post_box", "post_office")),
        Icon(path = "R.drawable.bench", functions = listOf("bench")),
        Icon(path = "R.drawable.drinking_water", functions = listOf("drinking_water")),
        Icon(path = "R.drawable.telephone", functions = listOf("telephone")),
        Icon(path = "R.drawable.toilets", functions = listOf("toilets")),
        Icon(path = "R.drawable.recycling", functions = listOf("recycling")),
        Icon(path = "R.drawable.waste_basket", functions = listOf("waste_basket")),
        Icon(path = "R.drawable.church", functions = listOf("place_of_worship")),
        Icon(path = "R.drawable.lodging", functions = listOf("hotel", "apartment", "guest_house", "hostel")),
        Icon(path = "R.drawable.office", functions = listOf("office")),
        Icon(path = "R.drawable.government", functions = listOf("government")),
        Icon(path = "R.drawable.bus_stop", functions = listOf("bus_stop")),
        Icon(path = "R.drawable.elevator", functions = listOf("elevator")),
        Icon(path = "R.drawable.traffic_signals", functions = listOf("traffic_signals")),
        Icon(path = "R.drawable.crossing", functions = listOf("crossing")),
        Icon(path = "R.drawable.memorial", functions = listOf("memorial")),
        Icon(path = "R.drawable.monument", functions = listOf("monument")),
        Icon(path = "R.drawable.castle_palace", functions = listOf("castle")),
        Icon(path = "R.drawable.dog_park", functions = listOf("dog_park")),
        Icon(path = "R.drawable.sport_fitness", functions = listOf("fitness_centre", "fitness_station", "sports_centre", "pitch")),
        Icon(path = "R.drawable.picnic_table", functions = listOf("picnic_table")),
        Icon(path = "R.drawable.playground", functions = listOf("playground")),
        Icon(path = "R.drawable.swimming", functions = listOf("swimming_pool", "water_park")),
        Icon(path = "R.drawable.train_station", functions = listOf("station")),
        Icon(path = "R.drawable.subway_entrance", functions = listOf("subway_entrance")),
        Icon(path = "R.drawable.tram_station", functions = listOf("tram_stop")),
        Icon(path = "R.drawable.shop", functions = listOf("bakery", "supermarket", "pastry")),
        Icon(path = "R.drawable.bakery", functions = listOf("bakery")),
        Icon(path = "R.drawable.supermarket", functions = listOf("supermarket")),
        Icon(path = "R.drawable.artwork", functions = listOf("artwork")),
        Icon(path = "R.drawable.gallery", functions = listOf("gallery")),
        Icon(path = "R.drawable.information", functions = listOf("information")),
        Icon(path = "R.drawable.museum", functions = listOf("museum"))
    )

    val iconCategories = listOf(
        "amenity",
        "tourism",
        "leisure",
        "historic",
        "highway",
        "public_transport",
        "railway",
        "shop",
        "office",
        "building"
    )

}


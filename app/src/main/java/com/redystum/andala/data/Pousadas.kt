package com.redystum.andala.data


data class Pousada(
    val id: Int,
    val external_id: Int,
    val name: String,
    val location: String,
    val coordinates: Pair<Double, Double>,
)

object Pousadas {
    // galery link
    // https://pousadasjuventude.e-gds.com/pousadasdajuventude/shoppingcart/hoteldetails.aspx?hotelid=62#tabs-3

    /*

Todas as Pousadas de Juventude estão incluídas?

Não. As Pousadas de Juventude de Melgaço, Viseu, Serra da Estrela, Braga, Setúbal e Arrifana não estão incluídas.
     */

    val listaPousadas = listOf(
        Pousada(
            0,
            62,
            "Abrantes",
            "Av. Engº Adelino Amaro da Costa, Abrantes, Portugal",
            Pair(39.45619855, -8.19621107)
        ),
        Pousada(
            1,
            71,
            "Alcoutim",
            "Alcoutim, Alcoutim, Portugal",
            Pair(37.47729547790751, -7.471006811881322)
        ),
        Pousada(
            2,
            72,
            "Alfeizerão",
            "Estrada Nacional 8, Alfeizerão, Portugal",
            Pair(39.50864003539933, -9.086277969980186)
        ),
        Pousada(
            3,
            70,
            "Alijó",
            "Rua da Costinha, 10, Alijó, Portugal",
            Pair(41.278391635899695, -7.476217553149511)
        ),
        Pousada(
            4,
            61,
            "Almada",
            "Quinta do Bucelinho - Pragal, Almada, Portugal",
            Pair(38.676935455050184, -9.178426696315935)
        ),
        Pousada(
            5,
            56,
            "Almograve",
            "Rua do Chafariz – Almograve, Odemira, Portugal",
            Pair(37.65373913580771, -8.791871876527093)
        ),
        Pousada(
            6,
            63,
            "Alvados",
            "Rua Carreiro da Lebre Nª8, Alvados, Portugal",
            Pair(39.553155140000264, -8.768679611434855)
        ),
        Pousada(
            7,
            64,
            "Areia Branca",
            "Praia da Areia Branca - Largo João Soldado, Lourinhã, Portugal",
            Pair(39.2674510865267, -9.335175486254013)
        ),
        Pousada(
            8,
            65,
            "Aveiro",
            "Rua das Pombas, Edifício do IPDJ, Aveiro, Portugal",
            Pair(40.632702231265945, -8.652598889991925)
        ),
        Pousada(
            9,
            55,
            "Beja",
            "Rua Prof. Janeiro Acabado, Beja, Portugal",
            Pair(38.00759448992386, -7.857578891779267)
        ),
//        Pousada(
//            10,
//            88,
//            "Braga",
//            "Rua de Santa Margarida, 6, Braga, Portugal",
//            Pair(41.55397425920146, -8.417104375271512)
//        ),
        Pousada(
            11,
            69,
            "Bragança",
            "Avenida 22 de Maio, Bragança, Portugal",
            Pair(41.80468291643435, -6.766343513031821)
        ),
        Pousada(
            12,
            66,
            "Castelo Branco",
            "Rua Dr. Francisco José Palmeiro, Edifício do IPDJ, Castelo Branco, Portugal",
            Pair(39.82149484765065, -7.5024469254534125)
        ),
        Pousada(
            13,
            67,
            "Coimbra",
            "Rua Dr. Henriques Seco, 14, Coimbra, Portugal",
            Pair(40.211262056089076, -8.414048942517404)
        ),
        Pousada(
            14,
            60,
            "Espinho",
            "Lugar de Sales – Silvade, Espinho, Portugal",
            Pair(40.99793305558849, -8.623312926933561)
        ),
        Pousada(
            15,
            54,
            "Évora",
            "Rua Miguel Bombarda, 40, Évora, Portugal",
            Pair(38.569911, -7.907249)
        ),
        Pousada(
            16,
            83,
            "Faro",
            "Rua da PSP, Edifício do IPDJ, Faro, Portugal",
            Pair(37.01454212680449, -7.926566575500882)
        ),
        Pousada(
            17,
            68,
            "Foz Côa",
            "Caminho Vicinal Currauteles, N.º 5, Vila Nova de Foz Côa, Portugal",
            Pair(41.09342442767973, -7.1403590464442885)
        ),
        Pousada(
            18,
            89,
            "Gerês",
            "Rua da Pousada 1, Gerês, Portugal",
            Pair(41.753174267697894, -8.203137951411836)
        ),
        Pousada(
            19,
            95,
            "Guarda",
            "Avenida Alexandre Herculano, Edificio IPDJ, Guarda, Portugal",
            Pair(40.535208913104, -7.273418786232478)
        ),
        Pousada(
            20,
            59,
            "Guimaraes",
            "Complexo Multifuncional de Couros, Largo do Cidade, 8, Guimarães, Portugal",
            Pair(41.43993376241851, -8.293142267324614)
        ),
//        Pousada(
//            21,
//            87,
//            "HI Arrifana Destination Hostel",
//            "Urbanização Arrifamar, lote 43\\44 - Praia da Arrifana (Caixa postal 804 A), Arrifana, Portugal",
//            Pair(37.294827498404054, -8.861069668280601)
//        ),
        Pousada(
            22,
            84,
            "Lagos",
            "Rua Lançarote de Freitas, 50, Lagos, Portugal",
            Pair(37.09900795, -8.67300079)
        ),
        Pousada(
            23,
            53,
            "Lisboa",
            "Rua Andrade Corvo, 46, Lisboa, Portugal",
            Pair(38.7303004, -9.1477593)
        ),
        Pousada(
            24,
            74,
            "Lousã",
            "Rua da Feira, Lousã, Portugal",
            Pair(40.11251419964681, -8.24192286722637)
        ),
//        Pousada(
//            25,
//            79,
//            "Melgaço",
//            "Complexo Desportivo Monte Prado, Melgaço, Portugal",
//            Pair(42.12172711128893, -8.272424723875545)
//        ),
        Pousada(
            26,
            82,
            "Oeiras",
            "Estrada Marginal, Parque Inatel, Oeiras, Portugal",
            Pair(38.67806787596943, -9.317600887362055)
        ),
        Pousada(
            27,
            78,
            "Ofir",
            "Alameda Bom Jesus - Fão, Esposende, Portugal",
            Pair(41.511151899733484, -8.767451144255054)
        ),
        Pousada(
            28,
            75,
            "Ovar",
            "Av. D. Manuel I (EN 327), Ovar, Portugal",
            Pair(40.87136799374957, -8.64507743626466)
        ),
        Pousada(
            29,
            52,
            "Parque das Nações",
            "Rua de Moscavide 71, Lisboa, Portugal",
            Pair(38.778423145679504, -9.097871152658534)
        ),
        Pousada(
            30,
            86,
            "Ponte de Lima",
            "Rua Papa João Paulo II, Ponte de Lima, Portugal",
            Pair(41.761040235580474, -8.5901920866333)
        ),
        Pousada(
            31,
            96,
            "Portalegre",
            "Av. do Bonfim, 7300, Portalegre, Portugal",
            Pair(39.30044669933044, -7.432499904851262)
        ),
        Pousada(
            32,
            57,
            "Portimao",
            "Rua Pousada de Juventude, Portimão, Portugal",
            Pair(37.152068935746364, -8.544304466514038)
        ),
        Pousada(
            33,
            58,
            "Porto",
            "Rua Paulo da Gama, 551, Porto, Portugal",
            Pair(41.14816480189619, -8.659061394279892)
        ),
        Pousada(
            34,
            76,
            "Santa Cruz",
            "Empreendimento Turístico “Mar Azul”-  Bloco 6, E.N. 247, Torres Vedras, Portugal",
            Pair(39.13082788567138, -9.374570063396227)
        ),
        Pousada(
            35,
            77,
            "São Pedro do Sul",
            "Rua Central, 818 Termas, Várzea de São Pedro do Sul, Portugal",
            Pair(40.7381925, -8.0914257)
        ),
//        Pousada(
//            36,
//            90,
//            "Serra da Estrela",
//            "Estrada Nacional 339 Penhas da Saúde, Serra da Estrela, Portugal",
//            Pair(40.31082180732389, -7.550620398307048)
//        ),
//        Pousada(
//            37,
//            91,
//            "Setubal",
//            "Largo José Afonso, 24-25, Setubal, Portugal",
//            Pair(38.52142878766899, -8.896552376140335)
//        ),
        Pousada(
            38,
            85,
            "Tavira",
            "Rua Miguel Bombarda, Nº 36 -38, Tavira, Portugal",
            Pair(37.12364906784261, -7.652481497365049)
        ),
        Pousada(
            39,
            80,
            "Viana do Castelo",
            "Rua de Limia, Viana do Castelo, Portugal",
            Pair(41.69591253413338, -8.818190581518682)
        ),
        Pousada(
            40,
            94,
            "Vila do Conde",
            "Avenida Bento de Freitas, 460, Vila do Conde, Portugal",
            Pair(41.352843777754515, -8.75013327784379)
        ),
        Pousada(
            41,
            81,
            "Vila Nova de Cerveira",
            "Rua Alto das Veigas, EN13, Vila Nova de Cerveira, Portugal",
            Pair(41.93777290764022, -8.745596180512454)
        ),
//        Pousada(
//            42,
//            92,
//            "Viseu",
//            "Rua Cônsul Aristides Sousa Mendes, Viseu, Portugal",
//            Pair(40.66045836460994, -7.904574076748201)
//        ),

        )
}

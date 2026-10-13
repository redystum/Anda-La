import requests
import bs4

"""
// https://pousadasjuventude.e-gds.com/pousadasdajuventude/
const parent = document.querySelector('#ctl00_ContentPlaceHolder1_Availability1_divHotelAvailability');

if (!parent) {
  console.error('Div pai não encontrada.');
} else {
  const hotels = Array.from(parent.querySelectorAll('.Hotel')).map(hotel => {
    // 1. Nome da pousada (limpa o texto dentro do h2 removendo tags filhas como o span das estrelas)
    const h2 = hotel.querySelector('.HotelDescription h2');
    const nome = h2 ? (h2.childNodes[0]?.textContent || h2.innerText).trim() : '';

    // 2. Localização (nó de texto solto dentro de .HotelDescription antes de .HotelMore)
    const descEl = hotel.querySelector('.HotelDescription');
    let localizacao = '';
    if (descEl) {
      const textNode = Array.from(descEl.childNodes).find(
        node => node.nodeType === Node.TEXT_NODE && node.textContent.trim().length > 0
      );
      localizacao = textNode ? textNode.textContent.trim() : '';
    }

    // 3. Link da Galeria
    const galleryAnchor = hotel.querySelector('a.lnkHotelGallery, a[id$="aPhotos"]');
    // .href retorna o link absoluto; se preferires o caminho relativo original, usa .getAttribute('href')
    let linkGaleria = galleryAnchor ? galleryAnchor.href : '';
    //https://pousadasjuventude.e-gds.com/pousadasdajuventude/shoppingcart/hoteldetails.aspx?hotelid=70#tabs-3
    linkGaleria = linkGaleria.split('?hotelid=').pop().split('#')[0]

    const image = hotel.querySelector('img');
    const imageUrl = image ? image.src : '';

    return {
      nome,
      localizacao,
      linkGaleria,
      imageUrl
    };
  });

  console.table(hotels);
  console.log(hotels);
}
"""

arr =  [
    {
      "nome": "Abrantes",
      "localizacao": "Av. Engº Adelino Amaro da Costa, Abrantes, Portugal",
      "linkGaleria": "62",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/62/abrantes-pousada.jpg"
    },
    {
      "nome": "Alcoutim",
      "localizacao": "Alcoutim, Alcoutim, Portugal",
      "linkGaleria": "71",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/71/capa.jpg"
    },
    {
      "nome": "Alfeizerão",
      "localizacao": "Estrada Nacional 8, Alfeizerão, Portugal",
      "linkGaleria": "72",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/72/default_hotel_image.jpg"
    },
    {
      "nome": "Alijó",
      "localizacao": "Rua da Costinha, 10, Alijó, Portugal",
      "linkGaleria": "70",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/70/alij%C3%B3-44.jpg"
    },
    {
      "nome": "Almada",
      "localizacao": "Quinta do Bucelinho - Pragal, Almada, Portugal",
      "linkGaleria": "61",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/61/almada-1.jpg"
    },
    {
      "nome": "Almograve",
      "localizacao": "Rua do Chafariz – Almograve, Odemira, Portugal",
      "linkGaleria": "56",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/56/capa.jpg"
    },
    {
      "nome": "Alvados",
      "localizacao": "Rua Carreiro da Lebre Nª8, Alvados, Portugal",
      "linkGaleria": "63",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/63/alvados-entrada-2.jpg"
    },
    {
      "nome": "Areia Branca",
      "localizacao": "Praia da Areia Branca - Largo João Soldado, Lourinhã, Portugal",
      "linkGaleria": "64",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/64/capa.jpg"
    },
    {
      "nome": "Aveiro",
      "localizacao": "Rua das Pombas, Edifício do IPDJ, Aveiro, Portugal",
      "linkGaleria": "65",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/65/aveiro-32.jpg"
    },
    {
      "nome": "Beja",
      "localizacao": "Rua Prof. Janeiro Acabado, Beja, Portugal",
      "linkGaleria": "55",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/55/capa.jpg"
    },
    {
      "nome": "Braga",
      "localizacao": "Rua de Santa Margarida, 6, Braga, Portugal",
      "linkGaleria": "88",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/88/capa.jpg"
    },
    {
      "nome": "Bragança",
      "localizacao": "Avenida 22 de Maio, Bragança, Portugal",
      "linkGaleria": "69",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/69/bragan%C3%A7a-72.jpg"
    },
    {
      "nome": "Castelo Branco",
      "localizacao": "Rua Dr. Francisco José Palmeiro, Edifício do IPDJ, Castelo Branco, Portugal",
      "linkGaleria": "66",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/66/capa.jpg"
    },
    {
      "nome": "Coimbra",
      "localizacao": "Rua Dr. Henriques Seco, 14, Coimbra, Portugal",
      "linkGaleria": "67",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/67/coimbra-pousada.jpg"
    },
    {
      "nome": "Espinho",
      "localizacao": "Lugar de Sales – Silvade, Espinho, Portugal",
      "linkGaleria": "60",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/60/capa.jpg"
    },
    {
      "nome": "Évora",
      "localizacao": "Rua Miguel Bombarda, 40, Évora, Portugal",
      "linkGaleria": "54",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/54/%C3%A9vora-10.jpg"
    },
    {
      "nome": "Faro",
      "localizacao": "Rua da PSP, Edifício do IPDJ, Faro, Portugal",
      "linkGaleria": "83",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/83/capa%20(1).jpg"
    },
    {
      "nome": "Foz Côa",
      "localizacao": "Caminho Vicinal Currauteles, N.º 5, Vila Nova de Foz Côa, Portugal",
      "linkGaleria": "68",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/68/foz-c%C3%B4a-52.jpg"
    },
    {
      "nome": "Gerês",
      "localizacao": "Rua da Pousada 1, Gerês, Portugal",
      "linkGaleria": "89",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/89/capa.jpg"
    },
    {
      "nome": "Guarda",
      "localizacao": "Avenida Alexandre Herculano, Edificio IPDJ, Guarda, Portugal",
      "linkGaleria": "95",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/95/capa.jpg"
    },
    {
      "nome": "Guimaraes",
      "localizacao": "Complexo Multifuncional de Couros, Largo do Cidade, 8, Guimarães, Portugal",
      "linkGaleria": "59",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/59/capa.jpg"
    },
    {
      "nome": "HI Arrifana Destination Hostel",
      "localizacao": "Urbanização Arrifamar, lote 43\\44 - Praia da Arrifana (Caixa postal 804 A), Arrifana, Portugal",
      "linkGaleria": "87",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/87/arrifana-46.jpg"
    },
    {
      "nome": "Lagos",
      "localizacao": "Rua Lançarote de Freitas, 50, Lagos, Portugal",
      "linkGaleria": "84",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/84/capa%20(1).jpg"
    },
    {
      "nome": "Lisboa",
      "localizacao": "Rua Andrade Corvo, 46, Lisboa, Portugal",
      "linkGaleria": "53",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/53/lisboa-centro-20.jpg"
    },
    {
      "nome": "Lousã",
      "localizacao": "Rua da Feira, Lousã, Portugal",
      "linkGaleria": "74",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/74/capa.jpg"
    },
    {
      "nome": "Melgaço",
      "localizacao": "Complexo Desportivo Monte Prado, Melgaço, Portugal",
      "linkGaleria": "79",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/79/232255457.jpg"
    },
    {
      "nome": "Oeiras",
      "localizacao": "Estrada Marginal, Parque Inatel, Oeiras, Portugal",
      "linkGaleria": "82",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/82/oeiras-12.jpg"
    },
    {
      "nome": "Ofir",
      "localizacao": "Alameda Bom Jesus - Fão, Esposende, Portugal",
      "linkGaleria": "78",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/78/capa.jpg"
    },
    {
      "nome": "Ovar",
      "localizacao": "Av. D. Manuel I (EN 327), Ovar, Portugal",
      "linkGaleria": "75",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/75/ovar-32.jpg"
    },
    {
      "nome": "Parque das Nações",
      "localizacao": "Rua de Moscavide 71, Lisboa, Portugal",
      "linkGaleria": "52",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/52/lisboa-pn-44.jpg"
    },
    {
      "nome": "Ponte de Lima",
      "localizacao": "Rua Papa João Paulo II, Ponte de Lima, Portugal",
      "linkGaleria": "86",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/86/capa.jpg"
    },
    {
      "nome": "Portalegre",
      "localizacao": "Av. do Bonfim, 7300, Portalegre, Portugal",
      "linkGaleria": "96",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/96/portalegre-10.jpg"
    },
    {
      "nome": "Portimao",
      "localizacao": "Rua Pousada de Juventude, Portimão, Portugal",
      "linkGaleria": "57",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/57/portim%C3%A3o-2.jpg"
    },
    {
      "nome": "Porto",
      "localizacao": "Rua Paulo da Gama, 551, Porto, Portugal",
      "linkGaleria": "58",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/58/porto-1.jpg"
    },
    {
      "nome": "Santa Cruz",
      "localizacao": "Empreendimento Turístico “Mar Azul”-  Bloco 6, E.N. 247, Torres Vedras, Portugal",
      "linkGaleria": "76",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/76/santa-cruz-6.jpg"
    },
    {
      "nome": "São Pedro do Sul",
      "localizacao": "Rua Central, 818 Termas, Várzea de São Pedro do Sul, Portugal",
      "linkGaleria": "77",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/77/capa.jpg"
    },
    {
      "nome": "Serra da Estrela",
      "localizacao": "Estrada Nacional 339 Penhas da Saúde, Serra da Estrela, Portugal",
      "linkGaleria": "90",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/90/serra-da-estrela-pousada.jpg"
    },
    {
      "nome": "Setubal",
      "localizacao": "Largo José Afonso, 24-25, Setubal, Portugal",
      "linkGaleria": "91",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/91/setubal-fachada.jpg"
    },
    {
      "nome": "Tavira",
      "localizacao": "Rua Miguel Bombarda, Nº 36 -38, Tavira, Portugal",
      "linkGaleria": "85",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/85/capa.jpg"
    },
    {
      "nome": "Viana do Castelo",
      "localizacao": "Rua de Limia, Viana do Castelo, Portugal",
      "linkGaleria": "80",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/80/capa%20(1).jpg"
    },
    {
      "nome": "Vila do Conde",
      "localizacao": "Avenida Bento de Freitas, 460, Vila do Conde, Portugal",
      "linkGaleria": "94",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/94/vila-do-conde-32.jpg"
    },
    {
      "nome": "Vila Nova de Cerveira",
      "localizacao": "Rua Alto das Veigas, EN13, Vila Nova de Cerveira, Portugal",
      "linkGaleria": "81",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/81/vncerveira-58.jpg"
    },
    {
      "nome": "Viseu",
      "localizacao": "Rua Cônsul Aristides Sousa Mendes, Viseu, Portugal",
      "linkGaleria": "92",
      "imageUrl": "https://pousadasjuventude.e-gds.com/webfrontendcorporate/images/92/capa.jpg"
    }

]

def getCords(id):
    link = f"https://pousadasjuventude.e-gds.com/pousadasdajuventude/shoppingcart/hoteldetails.aspx?hotelid={id}#tabs-2"
    lat = "#ctl00_ContentPlaceHolder1_lblLatitude"
    long = "#ctl00_ContentPlaceHolder1_lblLongitude"
    try:
      r = requests.get(link)
      soup = bs4.BeautifulSoup(r.text, 'html.parser')
      lat = soup.select_one(lat).text
      long = soup.select_one(long).text
    except Exception as _:
        return "Pair(0.0, 0.0)"
    print(f"Coords for {id}: {lat}, {long}")
    return f"Pair({lat}, {long})"
    

with open('pousadas.txt', 'w') as f:
    for i, p in enumerate(arr):
        print(f"Processing {p['nome']} ({i+1}/{len(arr)})...")
        cords = getCords(p['linkGaleria'])
        f.write(f""" Pousada(
            {i},
            {p['linkGaleria']},
            "{p['nome']}",
            "{p['localizacao']}",
            {cords},
            "{p['imageUrl']}"
        ),
""")



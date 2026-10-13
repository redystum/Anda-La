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
    const linkGaleria = galleryAnchor ? galleryAnchor.href : '';

    return {
      nome,
      localizacao,
      linkGaleria
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
    "linkGaleria": "62"
  },
  {
    "nome": "Alcoutim",
    "localizacao": "Alcoutim, Alcoutim, Portugal",
    "linkGaleria": "71"
  },
  {
    "nome": "Alfeizerão",
    "localizacao": "Estrada Nacional 8, Alfeizerão, Portugal",
    "linkGaleria": "72"
  },
  {
    "nome": "Alijó",
    "localizacao": "Rua da Costinha, 10, Alijó, Portugal",
    "linkGaleria": "70"
  },
  {
    "nome": "Almada",
    "localizacao": "Quinta do Bucelinho - Pragal, Almada, Portugal",
    "linkGaleria": "61"
  },
  {
    "nome": "Almograve",
    "localizacao": "Rua do Chafariz – Almograve, Odemira, Portugal",
    "linkGaleria": "56"
  },
  {
    "nome": "Alvados",
    "localizacao": "Rua Carreiro da Lebre Nª8, Alvados, Portugal",
    "linkGaleria": "63"
  },
  {
    "nome": "Areia Branca",
    "localizacao": "Praia da Areia Branca - Largo João Soldado, Lourinhã, Portugal",
    "linkGaleria": "64"
  },
  {
    "nome": "Aveiro",
    "localizacao": "Rua das Pombas, Edifício do IPDJ, Aveiro, Portugal",
    "linkGaleria": "65"
  },
  {
    "nome": "Beja",
    "localizacao": "Rua Prof. Janeiro Acabado, Beja, Portugal",
    "linkGaleria": "55"
  },
  {
    "nome": "Braga",
    "localizacao": "Rua de Santa Margarida, 6, Braga, Portugal",
    "linkGaleria": "88"
  },
  {
    "nome": "Bragança",
    "localizacao": "Avenida 22 de Maio, Bragança, Portugal",
    "linkGaleria": "69"
  },
  {
    "nome": "Castelo Branco",
    "localizacao": "Rua Dr. Francisco José Palmeiro, Edifício do IPDJ, Castelo Branco, Portugal",
    "linkGaleria": "66"
  },
  {
    "nome": "Coimbra",
    "localizacao": "Rua Dr. Henriques Seco, 14, Coimbra, Portugal",
    "linkGaleria": "67"
  },
  {
    "nome": "Espinho",
    "localizacao": "Lugar de Sales – Silvade, Espinho, Portugal",
    "linkGaleria": "60"
  },
  {
    "nome": "Évora",
    "localizacao": "Rua Miguel Bombarda, 40, Évora, Portugal",
    "linkGaleria": "54"
  },
  {
    "nome": "Faro",
    "localizacao": "Rua da PSP, Edifício do IPDJ, Faro, Portugal",
    "linkGaleria": "83"
  },
  {
    "nome": "Foz Côa",
    "localizacao": "Caminho Vicinal Currauteles, N.º 5, Vila Nova de Foz Côa, Portugal",
    "linkGaleria": "68"
  },
  {
    "nome": "Gerês",
    "localizacao": "Rua da Pousada 1, Gerês, Portugal",
    "linkGaleria": "89"
  },
  {
    "nome": "Guarda",
    "localizacao": "Avenida Alexandre Herculano, Edificio IPDJ, Guarda, Portugal",
    "linkGaleria": "95"
  },
  {
    "nome": "Guimaraes",
    "localizacao": "Complexo Multifuncional de Couros, Largo do Cidade, 8, Guimarães, Portugal",
    "linkGaleria": "59"
  },
  {
    "nome": "HI Arrifana Destination Hostel",
    "localizacao": "Urbanização Arrifamar, lote 43\\44 - Praia da Arrifana (Caixa postal 804 A), Arrifana, Portugal",
    "linkGaleria": "87"
  },
  {
    "nome": "Lagos",
    "localizacao": "Rua Lançarote de Freitas, 50, Lagos, Portugal",
    "linkGaleria": "84"
  },
  {
    "nome": "Lisboa",
    "localizacao": "Rua Andrade Corvo, 46, Lisboa, Portugal",
    "linkGaleria": "53"
  },
  {
    "nome": "Lousã",
    "localizacao": "Rua da Feira, Lousã, Portugal",
    "linkGaleria": "74"
  },
  {
    "nome": "Melgaço",
    "localizacao": "Complexo Desportivo Monte Prado, Melgaço, Portugal",
    "linkGaleria": "79"
  },
  {
    "nome": "Oeiras",
    "localizacao": "Estrada Marginal, Parque Inatel, Oeiras, Portugal",
    "linkGaleria": "82"
  },
  {
    "nome": "Ofir",
    "localizacao": "Alameda Bom Jesus - Fão, Esposende, Portugal",
    "linkGaleria": "78"
  },
  {
    "nome": "Ovar",
    "localizacao": "Av. D. Manuel I (EN 327), Ovar, Portugal",
    "linkGaleria": "75"
  },
  {
    "nome": "Parque das Nações",
    "localizacao": "Rua de Moscavide 71, Lisboa, Portugal",
    "linkGaleria": "52"
  },
  {
    "nome": "Ponte de Lima",
    "localizacao": "Rua Papa João Paulo II, Ponte de Lima, Portugal",
    "linkGaleria": "86"
  },
  {
    "nome": "Portalegre",
    "localizacao": "Av. do Bonfim, 7300, Portalegre, Portugal",
    "linkGaleria": "96"
  },
  {
    "nome": "Portimao",
    "localizacao": "Rua Pousada de Juventude, Portimão, Portugal",
    "linkGaleria": "57"
  },
  {
    "nome": "Porto",
    "localizacao": "Rua Paulo da Gama, 551, Porto, Portugal",
    "linkGaleria": "58"
  },
  {
    "nome": "Santa Cruz",
    "localizacao": "Empreendimento Turístico “Mar Azul”-  Bloco 6, E.N. 247, Torres Vedras, Portugal",
    "linkGaleria": "76"
  },
  {
    "nome": "São Pedro do Sul",
    "localizacao": "Rua Central, 818 Termas, Várzea de São Pedro do Sul, Portugal",
    "linkGaleria": "77"
  },
  {
    "nome": "Serra da Estrela",
    "localizacao": "Estrada Nacional 339 Penhas da Saúde, Serra da Estrela, Portugal",
    "linkGaleria": "90"
  },
  {
    "nome": "Setubal",
    "localizacao": "Largo José Afonso, 24-25, Setubal, Portugal",
    "linkGaleria": "91"
  },
  {
    "nome": "Tavira",
    "localizacao": "Rua Miguel Bombarda, Nº 36 -38, Tavira, Portugal",
    "linkGaleria": "85"
  },
  {
    "nome": "Viana do Castelo",
    "localizacao": "Rua de Limia, Viana do Castelo, Portugal",
    "linkGaleria": "80"
  },
  {
    "nome": "Vila do Conde",
    "localizacao": "Avenida Bento de Freitas, 460, Vila do Conde, Portugal",
    "linkGaleria": "94"
  },
  {
    "nome": "Vila Nova de Cerveira",
    "localizacao": "Rua Alto das Veigas, EN13, Vila Nova de Cerveira, Portugal",
    "linkGaleria": "81"
  },
  {
    "nome": "Viseu",
    "localizacao": "Rua Cônsul Aristides Sousa Mendes, Viseu, Portugal",
    "linkGaleria": "92"
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
            {cords}
        ),
""")



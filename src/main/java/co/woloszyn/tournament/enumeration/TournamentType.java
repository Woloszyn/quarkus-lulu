package co.woloszyn.tournament.enumeration;

public enum TournamentType {
    FIFA("https://s2-techtudo.glbimg.com/zv8T4DSQttaHgV9ukyRXxYFUoxo=/0x0:1211x686/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_08fbf48bc0524877943fe86e43087e7a/internal_photos/bs/2024/z/0/4lHWPHRauqqQqWBv7k6Q/imagem-2024-07-17-131248440.png"),
    BRAWL_STARS("https://s2-techtudo.glbimg.com/ijb4Qmm4NeipvEWLjrDN37vDRZw=/0x0:695x391/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_08fbf48bc0524877943fe86e43087e7a/internal_photos/bs/2019/r/5/CSQu99R6SWLDVvXOSuCw/brawl-stars-como-jogar-no-pc.jpg"),
    FUTEBOL("https://static.internacional.com.br/cms/assets/capa_159a1fae83.jpeg"),
    FUTEVOLEI("https://www12.senado.leg.br/noticias/materias/2025/02/20/futevolei-pode-se-tornar-modalidade-esportiva-oficial-no-brasil/whatsapp-image-2025-02-17-at-15-44-35.jpeg/mural/imagem_materia"),
    PING_PONG("https://babyherois.com.br/wp-content/uploads/2019/05/2019-05-24-curiosidades-sobre-ping-pong.jpg"),
    STREET_FIGHTER("https://br.web.img2.acsta.net/pictures/24/01/31/21/43/3453517.jpg"),
    CLASH_ROYALE("https://s2-techtudo.glbimg.com/y5btPgTaXppPRIsxbRNmrhe30Uo=/1200x/smart/filters:cover():strip_icc()/i.s3.glbimg.com/v1/AUTH_08fbf48bc0524877943fe86e43087e7a/internal_photos/bs/2020/H/U/i0quACSJqSNfiQOFvTfQ/clash-royale-duas-contas-tutorial-5.jpg"),
    LEAGUE_OF_LEGENDS("https://s2-techtudo.glbimg.com/RtjsGDcpoierInXQzo2Bh5TZL8M=/0x0:695x392/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_08fbf48bc0524877943fe86e43087e7a/internal_photos/bs/2021/p/0/c0xFphTR2gOnJphJpdrA/2015-09-10-league-of-legends-review-001.jpg"),
    DOTA_2("https://cdn.steamstatic.com/apps/dota2/images/dota2_social.jpg"),
    COUNTER_STRIKE("https://www.lance.com.br/outros-lances/wp-content/uploads/2025/06/games-shooter-counter-strike-2.jpg"),
    OVERWATCH("https://blz-contentstack-images.akamaized.net/v3/assets/blt2477dcaf4ebd440c/bltdabc3782553659f1/6785b50a1970a9f14eb5ccd7/xboxshowcase.png"),
    VOLEIBOL("https://static.todamateria.com.br/upload/vo/le/voleifeminino-cke.jpg"),
    BASQUETE("https://admin.cnnbrasil.com.br/wp-content/uploads/sites/12/2024/07/Pre_Olimpico_de-_Basquete-Masculino_Brasil_vence_Filipinas_vai_a_final_e_fica_a_um_jogo_de_Paris-e1720273472112.jpg?w=886"),
    CORRIDA("https://blog.minhasinscricoes.com.br/wp-content/uploads/2024/07/2024-blog-minhas-inscricoes-vida-de-atleta-corrida-de-rua.png"),
    CICLISMO("https://s3.static.brasilescola.uol.com.br/be/2024/04/pessoas-praticando-ciclismo-em-uma-estrada.jpg");

    private String imageUrl;

    TournamentType(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

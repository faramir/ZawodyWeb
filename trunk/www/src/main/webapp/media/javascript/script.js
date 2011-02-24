function confirm(e,args){
    jConfirm('Usun\u0105\u0107 '+args.nazwa+'?','Usuwanie',function(r){
        if(r)window.location=args.link;
    });
}
function confirm_rejudge(e,args){
    jConfirm('Sprawdzi\u0107 ponownie '+args.nazwa+'?','reJudge',function(r){
        if(r)window.location=args.link;
    });
}
function confirm_ghost(e,args){
    jConfirm('Ustawi\u0107 '+args.nazwa+' na '+args.widocznosc+'?','Ghost',function(r){
        if(r)window.location=args.link;
    });
}
function prompt(e,args){
    jPrompt('Filtrowanie wg '+args.nazwa+'. Podaj warto\u015b\u0107: ',args.def,'Filtrowanie',function(r){
        if(r)window.location=args.link+r+'.html';
    });
}
function prompt_ranking(e,args){
    jPrompt('Poka\u017c ranking z daty: ',args.def,'Poka\u017c ranking',function(r){
        if(r)window.location=args.link+r+'.html';
    });
}
function prompt_user(e,args){
    jPrompt('Podaj login: ',args.def,'Edycja u\u017cytkownika',function(r){
        if(r)window.location=args.link+r+args.suffix+'.html';
    });
}
function prompt_clock(e,args){
    jPrompt('Podaj warto\u015b\u0107: ',args.def,'Odliczanie do...',function(r){
        if(r)window.location=args.link+r+'.html';
    });
}
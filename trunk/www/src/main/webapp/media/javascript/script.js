function confirm(e, args)
{
    jConfirm('Usun\u0105\u0107 '+ args.nazwa + '?', 'Usuwanie', function(r){if(r){window.location=args.link;}});
}

function confirm_rejudge(e, args)
{
    jConfirm('Sprawdzi\u0107 ponownie '+ args.nazwa + '?', 'reJudge', function(r){if(r){window.location=args.link;}});
}
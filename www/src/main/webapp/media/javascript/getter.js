function createRequestObject(){

    var req;

    if(window.XMLHttpRequest){
        //For Firefox, Safari, Opera
        req = new XMLHttpRequest();
    }
    else if(window.ActiveXObject){
        //For IE 5+
        req = new ActiveXObject("Microsoft.XMLHTTP");
    }
    else{
        //Error for an old browser
        alert('Your browser is not IE 5 or higher, or Firefox or Safari or Opera');
    }

    return req;
}

//Make the XMLHttpRequest Object
var http = createRequestObject();
var res = null;

function sendRequest(method, url, handler){
    if(method == 'get' || method == 'GET'){
        http.open(method,url);
        if (typeof(handler)=='undefined') handler=handleResponse;
        http.onreadystatechange = handler;
        http.send(null);
    }
    return res;
}

function handleResponse(){
    if(http.readyState == 4 && http.status == 200){
        var response = http.responseText;
        if(response){
            res = response;
        }
    }
}


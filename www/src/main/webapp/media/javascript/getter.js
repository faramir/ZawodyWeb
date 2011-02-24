function createRequestObject(){
    if(window.XMLHttpRequest) {
        //For Firefox, Safari, Opera
        return new XMLHttpRequest();
    } else if(window.ActiveXObject){
        //For IE 5+
        return new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        //Error for an old browser
        alert('Your browser is not IE 5 or higher, or Firefox or Safari or Opera');
    }
    return null;
}

function ajaxSend(method, url, handler) {
    var http = createRequestObject();

    http.onreadystatechange = function () {
        handler(http);
    };

    this.doGet = function() {
        // make a HTTP GET request to the URL asynchronously
        http.open(method,url);
        http.send(null);
    }
}

function sendRequest(method, url, handler){
    if(method == 'get' || method == 'GET'){
        if (typeof(handler)=='undefined') handler=handleResponse;
        var ajax = new ajaxSend(method, url, handler);
        ajax.doGet();
    }
}
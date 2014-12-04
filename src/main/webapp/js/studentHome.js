$(document).ready(function(){
    $('.table').each(function(){
        sortTable($(this),2,null,true, 1);
    });
    
    //Binding for "Display Archived Data" check box.
    $("#displayArchivedData_check").on('change', function(){
        var urlToGo = $(location).attr('href');
        if(this.checked){
            gotoUrlWithParam(urlToGo, "displayarchive", "true");
        } else{
            gotoUrlWithParam(urlToGo, "displayarchive", "false");
        }
    });
});

/**
 * Go to the url with appended param and value pair
 */
function gotoUrlWithParam(url, param, value){
    var paramValuePair = param + "=" + value;
    if(!url.contains("?")){
        window.location.href = url + "?" + paramValuePair;
    } else if(!url.contains(param)){
        window.location.href = url + "&" + paramValuePair;
    } else if(url.contains(paramValuePair)){
        window.location.href = url;
    } else{
        var urlWithoutParam = removeParamInUrl(url, param);
        gotoUrlWithParam(urlWithoutParam, param, value);
    }
}

/**
 * Remove param and its value pair in the given url
 * Return the url withour param and value pair
 */
function removeParamInUrl(url, param){
    var indexOfParam = url.indexOf("?" + param);
    indexOfParam = indexOfParam == -1? url.indexOf("&" + param): indexOfParam;
    var indexOfAndSign = url.indexOf("&", indexOfParam + 1);
    var urlBeforeParam = url.substr(0, indexOfParam);
    var urlAfterParamValue = indexOfAndSign == -1? "": url.substr(indexOfAndSign);
    return urlBeforeParam + urlAfterParamValue;
}
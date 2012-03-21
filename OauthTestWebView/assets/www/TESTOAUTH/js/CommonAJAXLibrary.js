    var READYSTATE_UNINITIALIZED    = 0; 
    var READYSTATE_LOADING          = 1; 
    var READYSTATE_LOADED           = 2; 
    var READYSTATE_INTERACTIVE      = 3; 
    var READYSTATE_COMPLETE        = 4;      

    // XML HTTP Request 객체생성, 반환 
    function CreateXmlHttpRequestObject() 
    { 
        if(window.XMLHttpRequest) 
            xmlHttpObj = new XMLHttpRequest(); // for non-MS browsers 
        else 
        { 
            // for MS browser by version. 
            try 
            { 
                xmlHttpObj = new ActiveXObject("Microsoft.XMLHTTP"); 
            } 
            catch(e) 
           { 
                xmlHttpObj = new ActiveXObject("Msxml2.XMLHTTP"); 
            } 
        } 
      
        return xmlHttpObj; 
    }
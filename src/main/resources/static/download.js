window.registerExtension('pdfreport/download', function (options) {
		options.el.textContent = '';
    	var header = document.createElement('h2');  
    	header.style.textAlign="center";
    	header.textContent = 'Get quality info in a pdf document';
    	options.el.appendChild(header);
    	
    	var curWwwPath=window.document.location.href;
    	var pathName=options.location.pathname;
    	var pos=curWwwPath.indexOf(pathName);
    	var localhostPaht=curWwwPath.substring(0,pos);
		if(localhostPaht.substr(-1) =="/"){
    		localhostPaht=localhostPaht.substring(0,localhostPaht.length-1);
    	}
    	var file = document.createElement('h3');
    	file.style.textAlign="center";
    	var a = document.createElement("a");
	    var node = document.createTextNode("download");
	    a.appendChild(node);	 
	    a.setAttribute("href",localhostPaht+"/api/pdfreport/get?componentKey="+options.component.key);
	    file.appendChild(a);
	    options.el.appendChild(file);
	    return function () {
	    	options.el.textContent = '';
	   };
});

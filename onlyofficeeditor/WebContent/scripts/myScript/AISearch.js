var AISearch = {
	searchPassages: function (AISearchSentence, $http, success, error) {
		$http({
	    	method:'GET',
	    	url:'./CXF/REST/RichText/queryRichText/'+AISearchSentence,
		}).then(success, error);
    },
    
    filterDocByType: function(type,AISearchSentence, $http, success, error){
    	$http({
	    	method:'GET',
	    	url:'./CXF/REST/RichText/filterDocByType/'+type+'/'+AISearchSentence,
		}).then(success, error);
    }
}
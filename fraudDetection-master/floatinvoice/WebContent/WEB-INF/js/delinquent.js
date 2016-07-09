floatInvoiceListApp
 .controller('DelinquentCtrl', ['$scope', '$http', '$routeParams', 'fiService',
 			function($scope, $http, $routeParams, fiService){
	var acro = fiService.getAcronym();	
	$http.get('/floatinvoice/invoice/fraud?acro='+acro)
		 .success(function(data){
    			$scope.invoices = data.list;
    			console.log(data);
    	 });
}]);
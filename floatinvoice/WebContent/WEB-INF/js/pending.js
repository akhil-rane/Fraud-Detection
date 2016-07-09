 angular.module('floatInvoiceListApp')
 .controller('PendingCtrl', ['$scope', '$http', '$routeParams', 'fiService',
 			function($scope, $http, $routeParams, fiService){
 	var acro = fiService.getAcronym();			
	$http.get('/floatinvoice/invoice/pending?acro='+acro)
		 .success(function(data){
    			$scope.invoices = data.list;
    			$scope.sortField = 'amount';
   				 console.log(data);
    			});
}]);
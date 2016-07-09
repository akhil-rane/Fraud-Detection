 angular.module('buyerfloatInvoiceListApp')
 		.controller('BuyerRejectedCtrl', ['$scope', '$http', '$routeParams', 'buyerAcroNameService',
		  function($scope, $http, $routeParams, buyerAcroNameService){
		 	var acro = buyerAcroNameService.getAcronym();			
			$http.get('/floatinvoice/invoice/rejected?acro='+acro)
			 	 .success(function(data){
					$scope.invoices = data.list;
					$scope.sortField = 'amount';
						 console.log(data);
					});
		  }]);
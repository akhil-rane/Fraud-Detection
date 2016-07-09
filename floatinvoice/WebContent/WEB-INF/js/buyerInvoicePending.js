 angular.module('buyerfloatInvoiceListApp')
 		.controller('BuyerPendingCtrl', ['$scope', '$http', '$routeParams', 'buyerAcroNameService',
		  function($scope, $http, $routeParams, buyerAcroNameService){
		 	var acro = buyerAcroNameService.getAcronym();			
			$http.get('/floatinvoice/invoice/pending/approval?acro='+acro)
			 	 .success(function(data){
					$scope.invoices = data.list;
					$scope.sortField = 'amount';
					$scope.refresh = function(refId){
	   					console.log(refId);
						var index = -1;
						var invoiceList =  $scope.invoices;
						for (var i=0; i<invoiceList.length; i++){
							if( invoiceList[i].refId == refId ) {
									index = i;
									break;
							}
						}
						$scope.invoices.splice(index, 1);
					};
	   				$scope.approveInvoice = function (refId){
						$scope.refId = refId;
						var data = JSON.stringify({
							refId : $scope.refId,
							action : "0"            
						});
						$http.post('/floatinvoice/invoice/manage/approval', data)
							.success($scope.refresh($scope.refId));
					};	 
	   				$scope.rejectInvoice = function (refId){
						$scope.refId = refId;
						var data = JSON.stringify({
							refId : $scope.refId,
							action : "3"            
						});
						$http.post('/floatinvoice/invoice/manage/approval', data)
							.success($scope.refresh($scope.refId));
					};

					});
		  }]);
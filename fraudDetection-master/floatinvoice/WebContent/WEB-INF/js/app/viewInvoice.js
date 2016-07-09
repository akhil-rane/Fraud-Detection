var floatInvoiceListApp = angular.module('floatInvoiceListApp',[]);  
  
floatInvoiceListApp.controller('InvoiceCtrl',function ($scope, $http){

	$http.get('http://localhost:8080/floatinvoice/invoice/view?acro=COTIND').success(function(data){

		//$scope.invoiceGrid = data;
		console.log(data);

	});

});
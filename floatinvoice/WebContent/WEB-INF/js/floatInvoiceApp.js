 var floatInvoiceListAppHomePg = angular.module('floatInvoiceListAppHomePg',['ngRoute']);
    floatInvoiceListAppHomePg.config( function($routeProvider){
      $routeProvider.when('/invoices',{
        templateUrl:'/floatinvoice/welcomePage.jsp',
        controller:'UploadCtrl'
      })
      .when('/profile',{
        templateUrl:'/floatinvoice/html/profile.html',
        controller:'UnpaidCtrl'
      })
      .when('/reports',{
        templateUrl:'/floatinvoice/html/reports.html',
        controller:'PendingCtrl'
      })
      .when('/customers',{
        templateUrl:'/floatinvoice/html/customers.html',
        controller:'FundedCtrl'
      })
    });
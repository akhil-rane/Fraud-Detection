 var buyerfloatInvoiceListApp = angular.module('buyerfloatInvoiceListApp',
  ['angularUtils.directives.dirPagination','ngRoute', 'route-segment', 'view-segment', 'angularModalService',
  'ngMaterial', 'ngMessages']);

 /*       floatInvoiceListApp.config(function($mdThemingProvider) {
          $mdThemingProvider.theme('default')
            .primaryPalette('light-blue')
            .accentPalette('green');
        });*/
 
    buyerfloatInvoiceListApp.config(
      function($locationProvider, $routeProvider, $routeSegmentProvider){
      $routeSegmentProvider.
        when('/buyerAlleged','t0').
        when('/buyerPending','t1').
        when('/buyerApproved','t2').
        when('/buyerRejected','t3');

      $routeSegmentProvider
      .segment('t0', {
        templateUrl: '/floatinvoice/html/BuyerInvoiceAllegedPg.html',
        controller: 'BuyerAllegedCtrl'
      });

      $routeSegmentProvider
      .segment('t1', {
        templateUrl: '/floatinvoice/html/BuyerInvoicePendingPg.html',
        controller: 'BuyerPendingCtrl'
      });

      $routeSegmentProvider
      .segment('t2', {
        templateUrl: '/floatinvoice/html/BuyerInvoiceApprovedPg.html',
        controller: 'BuyerApprovedCtrl'
      });

      $routeSegmentProvider
      .segment('t3', {
        templateUrl: '/floatinvoice/html/BuyerInvoiceRejectedPg.html',
        controller: 'BuyerRejectedCtrl'
      });

    
    });
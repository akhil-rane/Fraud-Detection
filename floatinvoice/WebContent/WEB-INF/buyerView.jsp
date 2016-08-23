<!DOCTYPE html>
<html ng-app="buyerfloatInvoiceListApp">
<head>
  <title>Float Invoice - Accelerate Cashflow</title>
  <meta charset="utf-8">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  <link rel="stylesheet" href="css/angular-material.min.css">
  <style>
    table, th , td  {
      /*border: 1px solid grey;*/
      border-collapse: collapse;
      padding: 10px;
    }
    table tr:nth-child(odd) {
      background-color: #f1f1f1;
    }
    table tr:nth-child(even) {
      background-color: #ffffff;
    }
</style>
  <script src="js/jquery.min.js"></script>
  <script src="js/bootstrap.min.js"></script>
  <script src="js/angular.min.js"></script>
  <script src="js/angular-animate.min.js"></script>
  <script src="js/angular-aria.min.js"></script>
  <script src="js/angular-messages.min.js"></script>
  <!-- Angular Material Library -->
  <script src="js/angular-material.min.js"></script>
  <script src="js/angularModalService.js"></script>
  <script src="js/dirPagination.js"></script>
  <script src="js/angular-route.min.js"></script>
  <script src="js/appBuyerView.js"></script>
  <script src="js/buyerInvoicePending.js"></script>
  <script src="js/buyerInvoiceRejected.js"></script>
  <script src="js/buyerInvoiceApproved.js"></script>
  <script src="js/buyerInvoiceAlleged.js"></script>
  <script src="//cdnjs.cloudflare.com/ajax/libs/angular-route-segment/1.4.0/angular-route-segment.min.js"></script>
  <script>
    buyerfloatInvoiceListApp.service('buyerAcroNameService', function(){
      this.getAcronym = function(){
        return "${acronym}";
      };
    });
    buyerfloatInvoiceListApp.controller('BuyerTabsCtrl', ['$scope', '$location', 
      function ($scope, $location) {
      $scope.btabs = [
          /*{ link : '#/buyerAlleged', label : 'Alleged' },*/
          { link : '#/buyerPending', label: 'Pending'},
          { link : '#/buyerApproved', label: 'Approved'},
          { link : '#/buyerRejected', label : 'Rejected' }
        ];     
      var index = -1;
      var tabList =  $scope.btabs;
      for (var i=0; i<tabList.length; i++){
        if( tabList[i].link == '#'+$location.url() ) {
            index = i;
            break;
        }
      }
      $scope.bselectedTab = $scope.btabs[index];
      $scope.bsetSelectedTab = function(tab, alignment) {
        $scope.bselectedTab = tab;
      }
      $scope.btabClass = function(tab, alignment) {
        if ($scope.bselectedTab == tab) {
          return "active";
        } else {
          return "";
        }
      }
    }]);

  </script>
</head>
<body>

<div class="container">
    <br/>
    <br/>
    <div class="row">  
      <div class="col-sm-3">
          <img src = "img/logo.jpg" height=65/>                 
      </div>
      <div class="col-sm-7">
        <ul class="nav nav-tabs nav-pills nav-justified" ng-controller="BuyerTabsCtrl">
          <li ng-class="btabClass(btab, 'justified')" ng-repeat="btab in btabs" tab="btab"><a href="{{btab.link}}" ng-click="bsetSelectedTab(btab, 'justified')">{{btab.label}}</a></li>
        </ul>              
      </div>
      <div class="col-sm-2 well well-sm">
          <label> Welcome ${acronym} </label>
          <span style="float:right">
            <a href="/floatinvoice/logout" class="btn btn-primary" title="Logout" data-toggle="tooltip" >
            <span class="glyphicon glyphicon-log-out"></span></a>
          </span>
      </div>
    </div>
    <div class="row"> 
       <div class="col-sm-12">
        <div app-view-segment="0"></div>
      </div>
    </div>
    <br/>
  </div>
   
</body>

</html>

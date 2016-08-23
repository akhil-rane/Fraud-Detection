<!DOCTYPE html>
<html ng-app="finfloatInvoiceListApp">
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
  <script src="//kendo.cdn.telerik.com/2016.1.412/js/jquery.min.js"></script>
  <script src="js/bootstrap.min.js"></script>
  <script src="js/angular.min.js"></script>
  <script src="js/angular-animate.min.js"></script>
  <script src="js/angular-aria.min.js"></script>
  <script src="js/angular-messages.min.js"></script>
  <!-- Angular Material Library -->
  <script src="js/angular-material.min.js"></script>
<script src="https://rawgit.com/dwmkerr/angular-modal-service/master/dst/angular-modal-service.js"></script>

  <script src="js/moment.js"></script>

  <script src="js/dirPagination.js"></script>
  <script src="js/angular-route.min.js"></script>
  <script src="//cdnjs.cloudflare.com/ajax/libs/angular-route-segment/1.4.0/angular-route-segment.min.js"></script>
  <script src="js/appFinancierView.js"></script>
  <script src="js/finDashBdPg.js" ></script>
  <script src="js/finTxPage.js" ></script>
  <script src="js/finResearchView.js" ></script>
  <script src="js/directorInfo.js" ></script>
  <script src="js/finProfilePage.js" ></script>
  <script src="js/finInvoiceListView.js"></script>
  <script src="js/finApprovalQueueView.js"></script>
  <script src="js/finFundedListView.js" ></script>
  
<!-- <script src="img/cibil.jpg" ></script>
 --><link rel="stylesheet" href="//kendo.cdn.telerik.com/2016.1.412/styles/kendo.common-material.min.css" />
    <link rel="stylesheet" href="//kendo.cdn.telerik.com/2016.1.412/styles/kendo.material.min.css" />
    
    <script src="//kendo.cdn.telerik.com/2016.1.412/js/kendo.all.min.js"></script>

  <script>
    finfloatInvoiceListApp.service('acroNameService', function(){
      this.getAcronym = function(){
        return "${acronym}";
      };
    });
    finfloatInvoiceListApp.controller('TabsCtrl2', ['$scope', '$location', 
      function ($scope, $location) {

      $scope.vtabs = [
          { link : '#/findashboard', label: 'Dashboard'},
          { link : '#/finResearch', label: 'Research'},
          { link : '#/finTxPg', label : 'Transactions' }/*,
          { link : '#/finprofile', label: 'Profiles'}*/
        ];     
      var index = -1;
      var tabList =  $scope.vtabs;
      for (var i=0; i<tabList.length; i++){
        if( tabList[i].link == '#'+$location.url() ) {
            index = i;
            break;
        }
      }
      $scope.selectedTab = $scope.vtabs[index];

  /*    $scope.vsetSelectedTab = function(tab) {
        $scope.selectedTab = tab;
      }
      $scope.vtabClass = function(tab) {
        if ($scope.selectedTab == tab) {
          return "active";
        } else {
          return "";
        }
      }*/

    

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
        <ul class="nav nav-tabs nav-pills nav-justified" ng-controller="TabsCtrl2">
          <li ng-class="vtabClass(vtab, 'justified')" ng-repeat="vtab in vtabs" tab="vtab"><a href="{{vtab.link}}" ng-click="vsetSelectedTab(vtab, 'justified')">{{vtab.label}}</a></li>
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
 <!--      <div class="col-sm-2">
        <ul class="nav nav-tabs nav-pills nav-stacked" ng-controller="TabsCtrl2">
          <li ng-class="vtabClass(vtab, 'stacked')" ng-repeat="vtab in vtabs" tab="vtab"><a href="{{vtab.link}}" ng-click="vsetSelectedTab(vtab, 'stacked')">{{vtab.label}}</a></li>
        </ul>
      </div> -->
      <div class="col-sm-12">
        <div app-view-segment="0"></div>
      </div>
    </div>
    <br/>
  </div>
   
</body>

</html>

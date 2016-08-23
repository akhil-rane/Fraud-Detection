<!DOCTYPE html>
<html ng-app="adminfloatInvoiceListApp">
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
  <script src="js/appAdminView.js"></script>
  <script src="//cdnjs.cloudflare.com/ajax/libs/angular-route-segment/1.4.0/angular-route-segment.min.js"></script>
  <script>
    adminfloatInvoiceListApp.service('AdminAcroNameService', function(){
      this.getAcronym = function(){
        return "${acronym}";
      };
    });
    adminfloatInvoiceListApp.controller('AdminTabsCtrl', ['$scope', '$location', 
      function ($scope, $location) {
      $scope.adtabs = [         
          { link : '#/adHome', label : 'Home' },
          { link : '#/adDashBoard', label : 'Dashboard' }
        ];     
      var index = -1;
      var adtabList =  $scope.adtabs;
      for (var i=0; i<adtabList.length; i++){
        if( adtabList[i].link == '#'+$location.url() ) {
            index = i;
            break;
        }
      }
      $scope.adselectedTab = $scope.adtabs[index];
      $scope.adsetSelectedTab = function(tab, alignment) {
        $scope.adselectedTab = tab;
      }
      $scope.adtabClass = function(tab, alignment) {
        if ($scope.adselectedTab == tab) {
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
        <ul class="nav nav-tabs nav-pills nav-justified" ng-controller="AdminTabsCtrl">
          <li ng-class="adtabClass(btab, 'justified')" ng-repeat="adtab in adtabs" tab="adtab"><a href="{{adtab.link}}" ng-click="adsetSelectedTab(adtab, 'justified')">{{adtab.label}}</a></li>
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

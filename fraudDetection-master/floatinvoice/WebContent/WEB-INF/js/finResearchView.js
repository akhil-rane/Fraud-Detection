  angular.module('finfloatInvoiceListApp')
 
 .controller('FinResearchCtrl', ['$scope', '$location', 
      function ($scope, $location) {

      $scope.rtabs = [
          { link : '#/t2/prospects', label: 'Prospects'},
          { link : '#/t2/profiles', label: 'Profiles'},
          { link : '#/t2/industryTrends', label : 'Industry Trends' },
          { link : '#/t2/loanPerformance', label: 'Loan Performance'}
        ];     
      var index = -1;
      var tabList =  $scope.rtabs;
      for (var i=0; i<tabList.length; i++){
        if( tabList[i].link == '#'+$location.url() ) {
            index = i;
            break;
        }
      }
      $scope.selectedTab = $scope.rtabs[index];

      $scope.rsetSelectedTab = function(tab) {
        $scope.selectedTab = tab;
      }
      $scope.rtabClass = function(tab) {
        if ($scope.selectedTab == tab) {
          return "active";
        } else {
          return "";
        }
      }

    

    }]);
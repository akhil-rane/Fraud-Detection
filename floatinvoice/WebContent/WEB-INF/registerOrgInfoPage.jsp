
<!-- index.html -->
<!DOCTYPE html>
<html ng-app="validationApp" >
<head>
    <!-- CSS ===================== -->
    <!-- load bootstrap -->
    <link rel="stylesheet" href="../css/bootstrap.min.css"> 
     <link rel="stylesheet" href="../css/angular-material.min.css">
    <style>
        body    { padding-top:30px; }
    </style>
    
    <!-- JS ===================== -->
<!-- load angular -->
       <!-- Angular Material requires Angular.js Libraries -->
   <script src="../js/angular.min.js"></script>
   <script src="../js/angular-animate.min.js"></script>
   <script src="../js/angular-aria.min.js"></script>
   <script src="../js/angular-messages.min.js"></script>

  <!-- Angular Material Library -->
   <script src="../js/angular-material.min.js"></script>
    <script>
        // create angular app
        var validationApp = angular.module('validationApp', ['ngMaterial', 'ngMessages']);
         validationApp.config(function($mdThemingProvider) {
          $mdThemingProvider.theme('default')
            .primaryPalette('light-blue')
            .accentPalette('green');
        });
        // create angular controller
        validationApp.controller('mainController', function($scope, $window, $http) {
            $scope.user= {};
            $scope.states = ('Andhra Pradesh,Arunachal Pradesh,Assam,Bihar,Chhattisgarh,Goa,Gujarat,Haryana,Himachal Pradesh,Jammu and Kashmir,Jharkhand,Karnataka,Kerala,Madhya Pradesh,Maharashtra,Manipur,Meghalaya,Mizoram,Nagaland,Orissa,Punjab,Rajasthan,Sikkim,Tamil Nadu,Tripura,Uttarakhand,Uttar Pradesh,West Bengal,Tamil Nadu,Tripura,Andaman and Nicobar Islands,Chandigarh,Dadra and Nagar Haveli,Daman and Diu,Delhi,Lakshadweep,Pondicherry').split(',').map(function(state) {
            return {abbrev: state};
            });
            // function to submit the form after all validation has occurred            
            $scope.submitForm = function() {
                // check to make sure the form is completely valid
                if ($scope.userForm.$valid) {
                    
                    $http({
                        method:'POST',
                        url:'/floatinvoice/register/corpInfo',
                        data:$scope.user,
                        xhrFields: {
                            withCredentials: true
                        },
                        headers:{'Content-Type':'application/json'
                                 }
                        }).then(function successCallback(response) {
                            $window.location.replace('/floatinvoice/register/usrInfoPage');
                            console.log(response);
                          }, function errorCallback(response) {
                            console.log(response);
                      });
                }
            };
        });
    </script>
</head>

<!-- apply angular app and controller to our body -->
<body ng-controller="mainController">
<div class="container">
<div class="col-sm-8 col-sm-offset-2">
    <img src = "../img/logo.jpg" height=100/>
    <!-- PAGE HEADER -->
    <div>
        <h2 class="page-header">Add Company Information</h2>
    </div>
    <md-content layout-padding>
    <div>
      <form name="userForm" ng-submit="submitForm()" novalidate>
<!--         <div layout-gt-xs="row">
          <md-input-container class="md-block" flex-gt-xs>
            <label>Company (Disabled)</label>
            <input ng-model="user.company" disabled>
          </md-input-container>
          <md-datepicker ng-model="user.submissionDate" md-placeholder="Enter date">
          </md-datepicker>
        </div> -->
        <div layout-gt-xs="row"> 
              <md-input-container class="md-block" flex-gt-sm>
               <label>Name</label>
               <input type="text" name="compName" ng-model="user.compName">
              </md-input-container>
              <md-input-container class="md-block" flex-gt-sm>
               <label>Acronym</label>
               <input type="text" name="acronym"  ng-model="user.acronym" ng-minlength="3">
              </md-input-container>
        </div>
        <div layout-gt-xs="row">
         <md-input-container class="md-block" flex-gt-sm>
          <label>Phone Number</label>
          <input name="phoneNo" ng-model="user.phoneNo" ng-pattern="/^([0-9]{10})$/" />
          <div class="hint" ng-show="showHints">(##########)</div>
          <div ng-messages="userForm.phoneNo.$error" ng-hide="showHints">
            <div ng-message="pattern">########## - Please enter a valid phone number.</div>
          </div>
         </md-input-container>
        </div>    
       <div layout-gt-xs="row">   
        <md-input-container class="md-block" flex-gt-sm>
          <label>Street</label>
          <input type="text" name="street" ng-model="user.street">
        </md-input-container>
        <md-input-container class="md-block" flex-gt-sm>
          <label>City</label>
          <input type="text" name="city" ng-model="user.city">
        </md-input-container>
        </div>
        <div layout-gt-xs="row">
        <md-input-container class="md-block" flex-gt-sm>
            <label>State</label>
            <md-select ng-model="user.state">
              <md-option ng-repeat="state in states" value="{{state.abbrev}}">
                {{state.abbrev}}
              </md-option>
            </md-select>
          </md-input-container>
          <md-input-container class="md-block" flex-gt-sm>
            <label>Postal Code</label>
            <input name="zipCode" ng-model="user.zipCode" placeholder="12345"
                   required ng-pattern="/^[0-9]{5}$/" md-maxlength="5">
            <div ng-messages="userForm.zipCode.$error" role="alert" multiple>
              <div ng-message="required" class="my-message">You must supply a postal code.</div>
              <div ng-message="pattern" class="my-message">That doesn't look like a valid postal code.
              </div>
              <!-- <div ng-message="md-maxlength" class="my-message">
                Don't use the long version silly...we don't need to be that specific...
              </div> -->
            </div>
          </md-input-container>
          </div>
          <label class="md-block" flex-gt-sm>Profession Type</label>        
              <div class="radio">
                <label>
                    <input type="radio" name="orgType" value="BANK" ng-model="user.orgType">
                    Financier
                </label>
              </div>
                <div class="radio">
                <label>
                    <input type="radio" name="orgType" value="SELLER" ng-model="user.orgType">
                    Seller
                </label>
              </div>
              <div class="radio">
                <label>
                    <input type="radio" name="orgType" value="BUYER" ng-model="user.orgType">
                    Buyer
                </label>
              </div>
           <md-button type="submit" class="md-raised md-primary" ng-disabled="userForm.$invalid">Submit</md-button> 
     </form>
    </div>
  </md-content>
</div><!-- col-sm-8 -->
</div><!-- /container -->
</body>
</html>
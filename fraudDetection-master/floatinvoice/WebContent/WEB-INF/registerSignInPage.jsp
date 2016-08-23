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
            // function to submit the form after all validation has occurred            
            $scope.submitForm = function() {
                // check to make sure the form is completely valid
                if ($scope.userForm.$valid) {
                    
                    $http({
                        method:'POST',
                        url:'/floatinvoice/register/signInInfo',
                        data:$scope.user,
                        headers:{'Content-Type':'application/json'}
                        }).then(function successCallback(response) {
                            $window.location.replace('/floatinvoice/register/orgInfoPage');
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
<body ng-controller="mainController" onload='document.userForm.email.focus();'>
<div class="container">
<div class="col-sm-6 col-sm-offset-2">
    <img src = "../img/logo.jpg" height=100/>
    <!-- PAGE HEADER -->
    <div class="page-header"><h1>Sign Up</h1></div>
   
    <!-- FORM -->
    <!-- pass in the variable if our form is valid or invalid -->

    <!-- <form name="userForm" ng-submit="submitForm()" novalidate>
        <div class="form-group" ng-class="{ 'has-error' : userForm.email.$invalid }">
            <label>Email</label>
            <input type="email" name="email" class="form-control" ng-model="user.email">
            <p ng-show="userForm.email.$invalid" class="help-block">Enter a valid email.</p>
        </div>        
        <br/>
        <div class="form-group" ng-class="{ 'has-error' : userForm.passwd.$invalid }">
            <label>Password</label>
            <input type="password" name="passwd" class="form-control" ng-model="user.passwd" ng-minlength="3">
            <p ng-show="userForm.passwd.$error.minlength" class="help-block">Password is too short.</p>           
        </div>
        </br>
        <div class="form-group" ng-class="{ 'has-error' : userForm.passwd.$invalid }">
            <label>Confirm Password</label>
            <input type="password" name="confirmPasswd" class="form-control" ng-model="user.confirmPasswd" ng-minlength="3">
            <p ng-show="userForm.confirmPasswd.$error.minlength" class="help-block">Password is too short.</p>           
        </div>
        </br>
        <button type="submit" class="btn btn-primary" ng-disabled="userForm.$invalid">Submit</button>
    </form> -->
    <md-content layout-padding>
    <div>
      <form name="userForm" ng-submit="submitForm()" novalidate>
          <md-input-container class="md-block" flex-gt-sm>
            <label>Email</label>
            <input type="email" name="email" ng-model="user.email">
          </md-input-container>
          <md-input-container class="md-block" flex-gt-sm>
            <label>Password</label>
            <input type="password" name="passwd"  ng-model="user.passwd" ng-minlength="3">
          </md-input-container>
          <md-input-container class="md-block" flex-gt-sm>
            <label>Confirm Password</label>
            <input type="password" name="passwd"  ng-model="user.confirmPasswd" ng-minlength="3">
          </md-input-container>
          <md-button type="submit" class="md-raised md-primary" ng-disabled="userForm.$invalid">Submit</md-button>  
      </form>
    </div>
  </md-content>
</div><!-- col-sm-8 -->
</div><!-- /container -->
</body>
</html>
angular.module('floatInvoiceListApp')
        .directive('ngFiles', ['$parse', function ($parse) {
            function fn_link(scope, element, attrs) {
                console.log(" In fn_link");
                var onChange = $parse(attrs.ngFiles);
                element.on('change', function (event) {
                    onChange(scope, { $files: event.target.files });
                });
            };
            return {
                link: fn_link
            }
        } ])
        .controller('fileUploadController', ['$scope', '$http','fiService', function ($scope, $http, fiService) {

            var formdata = new FormData();
            $scope.getTheFiles = function ($files) {
                angular.forEach($files, function (value, key) {
                    formdata.append(key, value);
                });
            };

            // NOW UPLOAD THE FILES.
            $scope.uploadFiles = function () {
                console.log("hi !!");
                var request = {
                    method: 'POST',
                    url: '/floatinvoice/invoice/upload?acro='+fiService.getAcronym(),
                    data: formdata,
                    headers: {
                        'Content-Type': undefined
                    }
                };

                // SEND THE FILES.
                $http(request)
                    .success(function (d) {
                        alert(d);
                    })
                    .error(function () {
                    });
            }
        }]);

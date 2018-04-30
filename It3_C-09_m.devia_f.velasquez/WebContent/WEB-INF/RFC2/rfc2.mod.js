(function (ng) {
    var mod = angular.module('RFC2Mod', ['ui.router']);

    mod.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

            $stateProvider.state('rfc2', {
                views: {
                    inputView: {
                        templateUrl: "RFC2/rfc2.input.html",
                        controller: "RFC2Ctrl"
                    }
                }
            }).state('rfc2Response', {

                params: {
                    num: null
                },
                views: {
                    inputView: {
                        templateUrl: "RFC2/rfc2.input.html"
                    },
                    outputView: {
                        templateUrl: "RFC2/rfc2.output.html",
                        controller: "responseRFC2Ctrl"
                    }
                }
            });
        }]);

})(window.angular);
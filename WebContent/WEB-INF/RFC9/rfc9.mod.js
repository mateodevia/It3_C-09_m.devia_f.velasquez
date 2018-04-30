(function (ng) {
    var mod = angular.module('RFC9Mod', ['ui.router']);

    mod.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

            $stateProvider.state('rfc9', {
                views: {
                    inputView: {
                        templateUrl: "RFC9/rfc9.input.html",
                        controller: "RFC9Ctrl"
                    }
                }
            }).state('rfc9Response', {


                views: {
                    inputView: {
                        templateUrl: "RFC9/rfc9.input.html"
                    },
                    outputView: {
                        templateUrl: "RFC9/rfc9.output.html",
                        controller: "responseRFC9Ctrl"
                    }
                }
            });
        }]);

})(window.angular);
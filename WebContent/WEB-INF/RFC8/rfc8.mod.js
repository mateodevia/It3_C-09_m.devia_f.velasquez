(function (ng) {
    var mod = angular.module('RFC8Mod', ['ui.router']);

    mod.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

            $stateProvider.state('rfc8', {
                views: {
                    inputView: {
                        templateUrl: "RFC8/rfc8.input.html",
                        controller: "RFC8Ctrl"
                    }
                }
            }).state('rfc8Response', {

                params: {
                    
                    idAl: null
                },

                views: {
                    inputView: {
                        templateUrl: "RFC8/rfc8.input.html"
                    },
                    outputView: {
                        templateUrl: "RFC8/rfc8.output.html",
                        controller: "responseRFC8Ctrl"
                    }
                }
            });
        }]);

})(window.angular);
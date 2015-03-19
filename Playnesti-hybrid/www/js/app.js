// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.controllers' is found in controllers.js
angular.module('playnesti', ['ionic', 'playnesti.controllers', 'playnesti.services'])

    .run(function($ionicPlatform) {
        $ionicPlatform.ready(function() {
            // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
            // for form inputs)
            if (window.cordova && window.cordova.plugins.Keyboard) {
                cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
            }
            if (window.StatusBar) {
                // org.apache.cordova.statusbar required
                StatusBar.styleDefault();
            }
        });
    })

    .config(function($stateProvider, $urlRouterProvider) {
        $stateProvider

            .state('app', {
                url: "/app",
                abstract: true,
                templateUrl: "templates/menu.html",
                controller: 'MenuCtrl'
            })

            .state('app.program', {
                url: "/program",
                views: {
                    'menuContent': {
                        templateUrl: "templates/program.html",
                        controller: 'ProgramCtrl'
                    }
                }
            })

            .state('app.activities', {
                url: "/activities",
                views: {
                    'menuContent': {
                        templateUrl: "templates/activities.html",
                        controller: 'ActivitiesCtrl'
                    }
                }
            })

            .state('app.lanParty', {
                url: "/lan-party",
                views: {
                    'menuContent': {
                        templateUrl: "templates/lan-party.html",
                        controller: 'LanPartyCtrl'
                    }
                }
            })

            .state('app.gallery', {
                url: "/gallery",
                views: {
                    'menuContent': {
                        templateUrl: "templates/gallery.html",
                        controller: 'GalleryCtrl'
                    }
                }
            })

            .state('app.contacts', {
                url: "/contacts",
                views: {
                    'menuContent': {
                        templateUrl: "templates/contacts.html"
                    }
                }
            })

            .state('app.about', {
                url: "/about",
                views: {
                    'menuContent': {
                        templateUrl: "templates/about.html"
                    }
                }
            });

        /*.state('app.single', {
         url: "/playlists/:playlistId",
         views: {
         'menuContent': {
         templateUrl: "templates/playlist.html",
         controller: 'PlaylistCtrl'
         }
         }
         });*/
        // if none of the above states are matched, use this as the fallback
        $urlRouterProvider.otherwise('/app/program');
    });

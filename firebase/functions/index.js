const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

const DialogflowApp = require('actions-on-google').DialogflowApp;

function send_message(token, payload) {
    admin.messaging().sendToDevice(token, payload)
        .then(function(response) {
            console.log("Sent message: ", response);
            return 0;
        }).catch(function(error) {
            console.log("error ", error);
        });
}

exports.dialogflowFirebaseFulfillment = functions.https.onRequest((request, response) => {
    const app = new DialogflowApp({request, response});
    console.log('Dialogflow Request headers: ' + JSON.stringify(request.headers));
    console.log('Dialogflow Request body: ' + JSON.stringify(request.body));
    console.log('User_id: ' + request.body.originalRequest.data.user.userId);

    /*const db = admin.database();
    db.ref('users').orderByKey().once('value', function(snapshot) {
        snapshot.forEach((user) => {
            console.log(user.key);
            if (user.child('user_id').val() === request.body.originalRequest.data.user.userId) {
                console.log(user.child('reg_token').val());
                registration_token = user.child('reg_token').val();*/
                let action = request.body.result.action;
                const parameters = request.body.result.parameters;
                const contexts = request.body.result.contexts;

                var registration_token = "dqsuUa_GUHA:APA91bHlNzosy1ssVUx56Sz7ZG80lTo_Zbcic-szbQAeiJYCH6MIfijsf1iqgk-7UT7s8mHMRf-oI5aKirRmZ7-9unvQD1upo2-6S_gTYwCpw2fKAX_yLCHbJxjeem2A0s_I4ujacy6j";

                const actionHandlers = {
                    'input.welcome': () => {
                        app.ask('Hello world!');
                    },
                    'input.unknown': () => {
                        app.ask('I don\'t get it.');
                    },
                    'mouse.activate': () => {
                        send_message(registration_token, {data: {action: 'mouse.activate'}});
                        app.ask('Magnet mouse activated.');
                    },
                    'mouse.deactivate': () => {
                        send_message(registration_token, {data: {action: 'mouse.deactivate'}});
                        app.ask('Magnet mouse deactivated.');
                    },
                    'mouse.leftclick': () => {
                        send_message(registration_token, {data:{action: 'mouse.leftclick'}});
                        app.ask('Click!');
                    },
                    'mouse.rightclick': () => {
                        send_message(registration_token, {data:{action: 'mouse.rightclick'}});
                        app.ask('Click!');
                    },
                    'keyboard.type': () => {
                        send_message(registration_token, {data:{action: 'keyboard.type', typed: parameters.text}});
                        app.ask('Typed: "' + parameters.text + '"');
                    },
                    'keyboard.enter': () => {
                        send_message(registration_token, {data:{action: 'keyboard.enter'}});
                        app.ask('Pressed enter.');
                    },
                    'keyboard.pagedown': () => {
                        send_message(registration_token, {data:{action: 'keyboard.pagedown'}});
                        app.ask('Scrolling down.');
                    },
                    'keyboard.pageup': () => {
                        send_message(registration_token, {data:{action: 'keyboard.pageup'}});
                        app.ask('Scrolling up.');
                    },
                    'browser.back': () => {
                        send_message(registration_token, {data:{action: 'browser.back'}});
                        app.ask('Navigating to the previous page.');
                    },
                    'browser.forward': () => {
                        send_message(registration_token, {data:{action: 'browser.forward'}});
                        app.ask('Navigating to the next page.');
                    },
                    'browser.open': () => {
                        send_message(registration_token, {data:{action: 'browser.open'}});
                        app.ask('Browser opened.');
                    },
                    'browser.newtab': () => {
                        send_message(registration_token, {data:{action: 'browser.newtab'}});
                        app.ask('New tab opened.');
                    },
                    'browser.drive': () => {
                        send_message(registration_token, {data:{action: 'browser.drive'}});
                        app.ask('Opening your Google Drive.');
                    },
                    'media.pause': () => {
                        send_message(registration_token, {data:{action: 'media.pause'}});
                        app.ask('Pausing media.');
                    },
                    'media.play': () => {
                        send_message(registration_token, {data:{action: 'media.play'}});
                        app.ask('Playing media.');
                    },
                    'media.skip': () => {
                        send_message(registration_token, {data:{action: 'media.skip'}});
                        app.ask('Skipping to next.');
                    },
                    'media.volumeup': () => {
                        send_message(registration_token, {data:{action: 'media.volumeup'}});
                        app.ask('Turning it up.');
                    },
                    'media.volumedown': () => {
                        send_message(registration_token, {data:{action: 'media.volumedown'}});
                        app.ask('Turning it down.');
                    },
                    'default': () => {
                        app.ask('IDK what that action is');
                    }
                }

                if (!actionHandlers[action]){
                    action = 'default';
                }
                console.log(action);
                actionHandlers[action]();
            /*}
        });
    });*/
});

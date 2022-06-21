Vue.createApp({
    data() {
        return {
            email: "",
            signEmail:"",
            password: "",
            signPassword:"",
            name: "",
            lastName: "",

            clientLoggedIn: false,
        }
    },

    created() {
        axios.get("/api/authenticated")
        .then(res => {
            if (res.data == "authenticated") {
                this.clientLoggedIn = true
            } else if (res.data == "not authenticated") {
                this.clientLoggedIn = false
            }
        })
    },

    methods: {
        toggleNavbar() {
            const toggle = document.querySelector("#header-toggle"),
                nav = document.querySelector("#nav-bar"),
                headerpd = document.querySelector("#header")

            setTimeout(() => {
                toggle.classList.toggle("closer")
                if (toggle.classList.contains("closer")) {
                    toggle.textContent = "close"
                } else {
                    toggle.textContent = "menu"
                }
            }, 200);

            nav.classList.toggle('show-nav')
            headerpd.classList.toggle('body-pd')
        },
        
        loader(){
            let loader = document.querySelector('.loader-index')
            loader.classList.add('loader--active')
            window.setTimeout(function () {
                loader.classList.remove('loader--active')
            }, 750)
        },

        changeIndexScreen() {
            let main = document.querySelector('.index-main-content')
            let logIn = document.querySelector('.index-log-in')
            setTimeout(() => {
                main.classList.add('hidden')
                logIn.classList.add('activated')
            }, 750);

            this.loader()
        },

        changeIndexSignIn() {
            let main = document.querySelector('.index-main-content')
            let signIn = document.querySelector('.index-register')
            setTimeout(() => {
                main.classList.add('hidden')
                signIn.classList.add('activated')
            }, 750);

            this.loader()
        },

        signInOnLogScreen() {
            let main = document.querySelector('.index-log-in')
            let signIn = document.querySelector('.index-register')
            setTimeout(() => {
                main.classList.remove('activated')
                signIn.classList.add('activated')
            }, 750);

            this.loader()
        },

        logIn(){
            let wrong = document.querySelector(".wrong-password")

            axios.post('/api/login',`email=${this.email}&password=${this.password}`,
            {headers:{
                'content-type':'application/x-www-form-urlencoded',
                'accept': 'application/xml'
                }
            })
            .then(res => window.location.href = "../accounts.html")
            .catch((err) => {
                if (err.response) {
                    wrong.classList.add("activated")
                }
            })
        },

        signUp(){
            let wrong = document.querySelector(".email-used")

            axios.post('/api/clients',`firstName=${this.name}&lastName=${this.lastName}&email=${this.signEmail}&password=${this.signPassword}`,
            {headers:{
                'content-type':'application/x-www-form-urlencoded'
                }
            }).then((response) => {
                axios.post('/api/login',`email=${this.signEmail}&password=${this.signPassword}`,
                {headers:{
                    'content-type':'application/x-www-form-urlencoded'
                    }
                })
                .then(res => window.location.href = "../accounts.html")
            })
            .catch((err) => {
                if (err.response) {
                    wrong.classList.add("activated")
                }
            })
        },

        signOut(){
            axios.post('/api/logout')
            .then(res => window.location.href = "../index.html")
        },
    },
    
    mounted() {
        window.addEventListener('load', () => {
            let loader = document.querySelector('.loader-index')
            if (loader) {
                loader.classList.remove('loader--active')
            }
        })
    },
}).mount('#app')
const urlParams = new URLSearchParams(window.location.search);
const getID = urlParams.get('id');

Vue.createApp({
    data() {
        return {
            clientAccounts: [],
            datos: [],
            transactions: [],
            name: "",
            userName: "",
            userEmail: "",
            screenWidth: window.innerWidth,
        }
    },

    created() {
        axios.get("/api/clients/current")
            .then(res => {
                //Get all accounts, sort them and filter them by non disabled
                this.clientAccounts = res.data.accounts.sort((a, b) => a.id - b.id)
                this.clientAccounts = this.clientAccounts.filter(account => account.disabled == false)

                //Name, email and different dates
                this.name = res.data.firstName
                this.userName = res.data.firstName + " " + res.data.lastName
                this.userEmail = res.data.email
            })

        axios.get(`/api/accounts/${getID}`)
            .then(res => {
                //Get a specific account by id ^
                this.datos = res.data

                //Get transactions from specific account and sort them by most recent to least recent
                let arr = res.data.transaction.sort((a, b) => b.id - a.id)
                this.transactions = arr
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

        signOut(){
            //Sign out from account
            axios.post('/api/logout')
            .then(res => window.location.href = "/web/index.html")
        },

        formatDate(date) {
            //Format a recived date from a parameter to make it look prettier
            //Dates will be transaction date, format it to be DD/MM
            let newDate = new Date(date)
            let year = newDate.getFullYear()
            let month = newDate.getMonth()
            let day = newDate.getDate()
            let currentDate = new Date()
            //JS getMonth() is an array of twelve strings, since january is 
            //at position 0, to get the current month you have to add 1 
            month += 1

            if (month < 10) {
                month = "0" + month
            }

            if (day < 10) {
                day = "0" + day
            }

            //If the date is older than current year show format DD/MM/YYYY
            if (year < currentDate.getFullYear()) {
                return day + "/" + month + "/" + year
            }

            return day + "/" + month
        }
    },

    mounted() {
        //On window loaded remove a class from the loader to make it disappear 
        window.addEventListener('load', () => {
            let loader = document.querySelector('.loader-index')
            if (loader) {
                loader.classList.remove('loader--active')
            }
        })
    },
}).mount('#app')


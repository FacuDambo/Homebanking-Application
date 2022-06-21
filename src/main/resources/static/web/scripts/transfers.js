Vue.createApp({
    data() {
        return {
            data: [],
            accounts: [],

            userName: "",
            userEmail: "",

            originAccountOwn: "Select Account",
            destinationAccountOwn: "Select Account",
            categoryOwn: "Select Category",
            amountOwn: "",
            descriptionOwn: "",

            originAccountOther: "Select Account",
            destinationAccountOther: "",
            categoryOther: "Select Category",
            amountOther: "",
            descriptionOther: "",
        }   
    },

    created() {
        axios.get("/api/clients/current")
            .then(res => {
                //Get all the data from the logged in client
                this.data = res.data

                //Save that client's accounts, sort them and filter them by non disabled
                this.accounts = res.data.accounts
                this.accounts = this.accounts.sort((a, b) => b.id - a.id)
                this.accounts = this.accounts.filter(account => account.disabled == false) 

                //Name, email and different dates
                this.userName = res.data.firstName + " " + res.data.lastName
                this.userEmail = res.data.email
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

        changeScreenMain(main, div) {
            let main1 = document.querySelector(main)
            let change = document.querySelector(div)
            setTimeout(() => {
                main1.classList.add('hidden')
                change.classList.remove('hidden')
                change.classList.add('activated')
            }, 750);

            this.loader()
        },

        openModal(div){
            let modal = document.querySelector(div)
            modal.classList.remove("animate-modal-out")
            modal.classList.add("animate-modal-in")
        },

        closeModal(div){
            let modal = document.querySelector(div)
            modal.classList.add("animate-modal-out")
            setTimeout(() => {
                modal.classList.remove("animate-modal-in")
            }, 500);

            this.originAccountOwn = "Select Account"
            this.destinationAccountOwn = "Select Account"
            this.categoryOwn = "Select Category"
            this.amountOwn = ""
            this.descriptionOwn = ""

            this.originAccountOther = "Select Account"
            this.destinationAccountOther = ""
            this.categoryOther = "Select Category"
            this.amountOther = ""
            this.descriptionOther = ""
        },

        sendToSelf(){
            let transaction = document.querySelector(".modal-data")
            let loader = document.querySelector(".lds-ring")
            let error = document.querySelector(".error-box")
            let success = document.querySelector(".success-box")

            axios.post("/api/transactions", 
            `amount=${this.amountOwn}&description=${this.descriptionOwn}&category=${this.categoryOwn}&origin=${this.originAccountOwn}&destination=${this.destinationAccountOwn}` ,
            {headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(res => {
                transaction.classList.add("hidden")
                loader.classList.add("display-IB")

                setTimeout(() => {
                    loader.classList.remove("display-IB")
                    success.classList.remove("hidden")
                }, 2000);
            })
            .catch(err => {
                transaction.classList.add("hidden")
                loader.classList.add("display-IB")

                setTimeout(() => {
                    loader.classList.remove("display-IB")
                    error.classList.remove("hidden")
                }, 2000);
            })
        },

        sendToOther(){
            let transaction = document.querySelector(".modal-data2")
            let loader = document.querySelector(".lds-ring2")
            let error = document.querySelector(".error-box2")
            let success = document.querySelector(".success-box2")

            axios.post("/api/transactions", 
            `amount=${this.amountOther}&description=${this.descriptionOther}&category=${this.categoryOther}&origin=${this.originAccountOther}&destination=${this.destinationAccountOther}` ,
            {headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(res => {
                transaction.classList.add("hidden")
                loader.classList.add("display-IB")

                setTimeout(() => {
                    loader.classList.remove("display-IB")
                    success.classList.remove("hidden")
                }, 2000);
            })
            .catch(err => {
                transaction.classList.add("hidden")
                loader.classList.add("display-IB")

                setTimeout(() => {
                    loader.classList.remove("display-IB")
                    error.classList.remove("hidden")
                }, 2000);
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
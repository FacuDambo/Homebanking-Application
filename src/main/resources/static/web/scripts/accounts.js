Vue.createApp({
    data() {
        return {
            datos: [],
            loans: [],
            datosDolar: [],
            clientAccounts: [],
            transactionAuxiliar: [],
            balanceActual: [],
            totalSpent: [],
            totalEarned: [],
            allTransactions: [],
            fiveTransactions: [],
            filterTransactions: [],
            selectAccounts: [],
            filterSelectedAccounts: [],
            finalArray: [],
            isSelected: false,
            accountType: "",
            accountID: "",
            name: "",
            userName: "",
            userEmail: "",
            currentDate: "",
            threeDays: "",
            oneWeek: "",
            oneMonth: "",
            getDate: "All values",
        }
    },

    created() {
        axios.get("http://localhost:8080/api/clients/current")
            .then(res => {
                this.datos = res.data
                this.loans = res.data.loans

                this.clientAccounts = res.data.accounts.sort((a, b) => a.id - b.id)
                this.clientAccounts = this.clientAccounts.filter(account => account.disabled == false)

                //Get all transactions into a single array to work with them
                this.clientAccounts.forEach(acc => this.transactionAuxiliar.push(acc.transaction))
                this.transactionAuxiliar.forEach(trans => trans.forEach(par => this.allTransactions.push(par)))

                this.allTransactions.sort((a, b) => b.id - a.id)
                this.fiveTransactions = this.allTransactions.slice(0, 5);

                if (this.selectAccounts.length == 0) {
                    this.finalArray = this.fiveTransactions
                }

                //Name, email and different dates
                this.name = res.data.firstName
                this.userName = res.data.firstName + " " + res.data.lastName
                this.userEmail = res.data.email
                //Current date
                this.currentDate = new Date()
                //Three days
                this.threeDays = new Date()
                this.threeDays.setDate(this.currentDate.getDate() - 3)
                //Seven Days
                this.oneWeek = new Date()
                this.oneWeek.setDate(this.currentDate.getDate() - 7)
                //Thirty days
                this.oneMonth = new Date()
                this.oneMonth.setDate(this.currentDate.getDate() - 30)
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

        getAccountOnClick(account) {
            let arr = account.transaction.sort((a, b) => b.id - a.id)
            this.finalArray = arr
            this.accountID = account.id

            let container = document.querySelector(".transactions")
            if (window.innerWidth < 1024) {
                container.scrollIntoView()
            }
        }, 
        
        signOut(){
            axios.post('/api/logout')
            .then(res => window.location.href = "http://localhost:8080/web/index.html")
        },

        createAccountView(id){
            let noAccount = document.querySelector("#noAccount" + id)
            let selectAccount = document.querySelector("#selectAccount" + id)
            noAccount.classList.add("hidden")
            selectAccount.classList.remove("hidden")
        },

        createAccount(typeAccount){
            axios.post("http://localhost:8080/api/clients/current/accounts/", `accountType=${typeAccount}`)
                .then(res => {
                    let loader = document.querySelector('.loader-index')
                    loader.classList.add('loader--active')
                    setTimeout(() => {
                        window.location.reload()
                    }, 750);
                })
        },

        scrollHorizontally(event, classToChange){
            if (window.innerWidth >= 1024) {
                event.preventDefault()
                let scroll = document.querySelector(classToChange)
                scroll.scrollLeft += event.deltaY;
            }
        },

        toggleAccountSettings(id){
            let account = document.querySelector("#id-" + id)
            let title = document.querySelector(".title" + id)
            let balance = document.querySelector(".balance" + id)
            let anchor = document.querySelector(".anchor" + id)
            let type = document.querySelector(".accountType" + id)
            let transactions = document.querySelector("#transaction" + id)
            let button = document.querySelector("#button" + id)

            account.classList.toggle("dashboard-settings")
            title.classList.toggle("hidden")
            balance.classList.toggle("hidden")
            anchor.classList.toggle("hidden")
            type.classList.toggle("hidden")
            transactions.classList.toggle("hidden")
            button.classList.toggle("hidden")
        },

        disableAccount(id) {
            axios.patch("/api/clients/current/accounts", `id=${id}`)
            .then(res => {
                let loader = document.querySelector('.loader-index')
                    loader.classList.add('loader--active')
                    setTimeout(() => {
                        window.location.reload()
                    }, 750);
            })
            .catch(err => console.log(err))
        },

        getCategoryTotal(category) {
            let filteredTransactions = this.filterTransactions.filter(t => t.category == category)
            let totalTransaction = filteredTransactions.map(t => t.amount).reduce((a, b) => a + b, 0)

            return totalTransaction
        },
    },

    computed: {
        changeTransactionData() {
            if (this.getDate == "3 days") {
                this.filterTransactions = this.allTransactions.filter(param => {
                    let date = new Date(param.creationDate);
                    return (date >= this.threeDays && date <= this.currentDate)
                })
            }

            if (this.getDate == "7 days") {
                this.filterTransactions = this.allTransactions.filter(param => {
                    let date = new Date(param.creationDate);
                    return (date >= this.oneWeek && date <= this.currentDate)
                })
            }
            if (this.getDate == "30 days") {
                this.filterTransactions = this.allTransactions.filter(param => {
                    let date = new Date(param.creationDate);
                    return (date >= this.oneMonth && date <= this.currentDate)
                })
            }
            if (this.getDate == "All values") {
                this.filterTransactions = this.allTransactions
            }

            //Total spent, total earned, remaining money and currency type and format worked with filtered transactions
            const totalBalance = this.clientAccounts.map(p => p.balance).reduce((a, b) => a + b, 0)
            const currency = {
                style: 'currency',
                currency: 'USD'
            };
            const amountSpent = this.filterTransactions.filter(p => p.type == "DEBIT").map(debit => debit.amount).reduce((a, b) => a + b, 0)
            const amountEarned = this.filterTransactions.filter(p => p.type == "CREDIT").map(debit => debit.amount).reduce((a, b) => a + b, 0)
            const totalAmount = totalBalance + amountSpent
            this.totalSpent = new Intl.NumberFormat('en-US', currency).format(amountSpent);
            this.totalEarned = new Intl.NumberFormat('en-US', currency).format(amountEarned);
            this.balanceActual = new Intl.NumberFormat('en-US', currency).format(totalAmount);
        }
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
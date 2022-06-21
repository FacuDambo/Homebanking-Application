Vue.createApp({
    data() {
        return {
            data: [],
            accounts: [],
            userName: "",
            userEmail: "",

            loanTypes: [],
            mortgage: [],
            personal: [],
            car: [],
            selectLoan: "Select loan type",
            selectLoanType: "",
            paymentAmount: "",
            solicitedAmount: "",
            selectedAccount: "Select account",
            errorMessage: "",
            percentage: "",
        }
    },

    created() {
        axios.get("/api/clients/current")
            .then(res => {
                this.data = res.data

                this.accounts = res.data.accounts
                this.accounts = this.accounts.sort((a, b) => b.id - a.id)
                this.accounts = this.accounts.filter(account => account.disabled == false)

                //Name, email and different dates
                this.userName = res.data.firstName + " " + res.data.lastName
                this.userEmail = res.data.email
            })

        axios.get("/api/loans")
            .then(res => {
                this.loanTypes = res.data

                this.mortgage = res.data[0].payments
                this.personal = res.data[1].payments
                this.car = res.data[2].payments
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

        loader() {
            let loader = document.querySelector('.loader-index')
            loader.classList.add('loader--active')
            window.setTimeout(function () {
                loader.classList.remove('loader--active')
            }, 750)
        },

        changeData(loan) {
            this.selectLoan = loan
            this.selectLoanType = this.selectLoan.name
            this.solicitedAmount = ''
            this.percentage = loan.percentage

            let container = document.querySelector("#loan-form-box");
            let style = document.querySelector(".radio-payment-box")
            style.classList.add("click-animation")
            container.scrollIntoView()
        },

        triggerAnimation(div) {
            let style = document.querySelector(div)
            style.classList.add("click-animation")
        },

        resetValue(element) {
            let el = element.target
            if (el.value != "") {
                if (parseInt(el.value) < parseInt(el.min)) {
                    el.value = el.min;
                    this.solicitedAmount = parseInt(el.min)
                }
                if (parseInt(el.value) > parseInt(el.max)) {
                    el.value = el.max;
                    this.solicitedAmount = parseInt(el.max)
                }
            }
        },

        toggleLoanRecap() {
            let formLoan = document.querySelector(".loan-form")
            let formRecap = document.querySelector(".loan-recap")

            if (formRecap.classList.contains("hidden")) {
                formLoan.classList.add("hidden")
                formRecap.classList.remove("hidden")
            } else if (formLoan.classList.contains("hidden")) {
                formLoan.classList.remove("hidden")
                formRecap.classList.add("hidden")
            }
        },

        requestLoan(){
            let loaderBox = document.querySelector(".loader-box")
            let form = document.querySelector(".loan-recap")
            let approved = document.querySelector(".approved")
            let declined = document.querySelector(".declined")

            let obj = {
                id: this.selectLoanType == "Mortgage" ? 1 : this.selectLoanType == "Personal" ? 2 : 3,
                amount: this.solicitedAmount,
                payments: this.paymentAmount,
                number: this.selectedAccount,
            }

            axios.post('/api/loans', obj)
            .then(res=> {
                loaderBox.classList.add("display-IB")
                form.classList.add("hidden")

                setTimeout(() => {
                    loaderBox.classList.remove("display-IB")
                    approved.classList.add("display-IB")
                }, 5000);
            })
            .catch(err=> {
                this.errorMessage = err.response.data
                loaderBox.classList.add("display-IB")
                form.classList.add("hidden")

                setTimeout(() => {
                    loaderBox.classList.remove("display-IB")
                    declined.classList.add("display-IB")
                }, 5000);

            })
        },
        
        signOut(){
            axios.post('/api/logout')
            .then(res => window.location.href = "/web/index.html")
        },
    },
    
    computed: {
        calcPayment() {
            let pay = ((this.solicitedAmount * this.percentage) / 100) + this.solicitedAmount
            return pay
        },

        calcTotal() {
            let total = ((this.solicitedAmount * this.percentage) / 100) + this.solicitedAmount
            let perMonth = total / this.paymentAmount
            return perMonth.toFixed(2)
        },

        changeLoanData(){
            this.selectLoanType = this.selectLoan.name
            this.percentage = this.selectLoan.percentage
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
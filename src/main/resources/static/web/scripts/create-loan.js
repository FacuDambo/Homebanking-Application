Vue.createApp({
    data() {
        return {
            loanName: "",
            loanAmount: "",
            loanPayment: "",
            loanPercentage: "",
            loanPaymentArray: [],
            removePayment: "",
            errorMsg: "",
        }
    },

    created() {
    },

    methods: {
        resetValue(element) {
            let el = element.target

            if (el.value != "") {
                if (parseInt(el.value) < parseInt(el.min)) {
                    el.value = el.min;
                    if (el.name == "loanPayments") {
                        this.loanPayment = parseInt(el.min)
                    } else if(el.name == "loanPercentage") {
                        this.loanPercentage = parseInt(el.min)
                    } else if (el.name == "loanAmount") {
                        this.loanAmount = parseInt(el.min)
                    }
                }

                if (parseInt(el.value) > parseInt(el.max)) {
                    el.value = el.max;
                    if (el.name == "loanPayments") {
                        this.loanPayment = parseInt(el.max)
                    } else if(el.name == "loanPercentage") {
                        this.loanPercentage = parseInt(el.max)
                    } else if (el.name == "loanAmount") {
                        this.loanAmount = parseInt(el.max)
                    }
                }
            }

            if (el.value == "") {
                el.value = el.min;
                if (el.name == "loanPayments") {
                    this.loanPayment = parseInt(el.min)
                } else if(el.name == "loanPercentage") {
                    this.loanPercentage =parseInt(el.min)
                } else if (el.name == "loanAmount") {
                    this.loanAmount = parseInt(el.min)
                }
            }
        },

        addPayment() {
            if (this.loanPayment == "") {
                this.loanPayment = 1
            }
            if (!this.loanPaymentArray.includes(this.loanPayment)) {
                this.loanPaymentArray.push(this.loanPayment)
            }

            this.loanPayment = ""
        },

        removePaymentfunction(){
            this.loanPaymentArray = this.loanPaymentArray.filter(item => item != this.removePayment)
            this.loanPaymentArray = this.loanPaymentArray.sort((a, b) => a - b)
        },

        createLoan(){
            let object = {
                name: this.loanName,
                amount: this.loanAmount,
                percentage: this.loanPercentage,
                payments: this.loanPaymentArray
            }
            axios.post('/api/loans/create', object)
            .then(res => {
                let success = document.querySelector("#created")
                success.classList.remove("hidden")

                setTimeout(() => {
                    window.location.reload()
                }, 5000);
            })
            .catch(err => {
                let error = document.querySelector("#error")
                error.classList.remove("hidden")
                this.errorMsg = err.response.data
            })
        }
    },

    computed: {
        sortPayments(){
            this.loanPaymentArray = this.loanPaymentArray.sort((a, b) => a - b)
        }
    },

    mounted() {
    },
}).mount('#app')
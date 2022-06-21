Vue.createApp({
    data() {
        return {
            allCards: [],
            debit: [],
            credit: [],
            clientAccounts: [],
            transactionAuxiliar: [],
            userName: "",
            userEmail: "",
            currentDate: new Date(),
            
            cardHolder:"#### ####",
            cardName: "",
            cardType:"###",
            cardNumber: "################",
            cardColor: "###",
            cardCVV: "###",
            cardFromDate: "",
            cardThruDate: "",
            cardID: "",
        }
    },

    created() {
        axios.get("/api/clients/current")
            .then(res => {
                this.allCards = res.data.cards
                this.allCards.sort((a, b) => a.cardType - b.cardType)
                this.allCards = this.allCards.filter(card => card.disabled == false)

                res.data.cards.forEach(cards => cards.cardType == 'DEBIT' && !cards.disabled ? this.debit.push(cards) : cards.cardType == 'CREDIT' && !cards.disabled ? this.credit.push(cards) : null)

                //Name, email and different dates
                this.userName = res.data.firstName + " " + res.data.lastName
                this.userEmail = res.data.email
            })
    },

    methods: {
        changeCardData(holder, name, type, number, color, cvv, from, thru, id){
            setTimeout(() => {
                this.cardHolder = holder;
                this.cardName = name;
                this.cardType = type;
                this.cardNumber = number;
                this.cardColor = color;
                this.cardCVV = cvv;
                this.cardFromDate = from;
                this.cardThruDate = thru;
                this.cardID = id;
            }, 750);

            
            let container = document.querySelector("#card-data");
            if (window.innerWidth < 1024) {
                container.scrollIntoView()
            }

            let loader = document.querySelector('.loader-index')
            loader.classList.add('loader--active')
            setTimeout(() => {
                loader.classList.remove('loader--active')
            }, 750);
        },

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

        splitCardNumber(number){
            let num = number.toString().split("");
            num.splice(4, 0, ' ');
            num.splice(9, 0, ' ');
            num.splice(14, 0, ' ');
            let complete = num.join("")

            return complete
        },

        copyCardNumber(){
            navigator.clipboard.writeText(this.cardNumber);
            let msg = document.querySelector(".message")
            msg.classList.remove("shown")
            setTimeout(() => {
                msg.classList.add("shown")
            }, 1500);
        },

        formatDate(date){
            let fulldate = new Date(date)
            let month = fulldate.getMonth() + 1
            month < 10 ? "0" + month : month
            let year = fulldate.getFullYear().toString().slice(-2)
            let formatedDate = month + "/" + year

            return formatedDate
        },

        getCVV(cvv){
            if (cvv < 10) {
                return "00" + cvv
            } else if (cvv < 100 && cvv >= 10) {
                return "0" + cvv
            } else {
                return cvv
            }
        },

        cardPreview(num){
            let number = num.toString()
            let sliced = number.slice(0, 4)
            
            return sliced
        },

        getCardType(number) {
            let num = number.toString().split("");
            return num[0]
        },

        signOut(){
            axios.post('/api/logout')
            .then(res => window.location.href = "../index.html")
        },

        disableCard() {
            axios.patch('/api/clients/current/cards', `id=${this.cardID}`)
            .then(res => window.location.reload())
        },

        toggleDelete() {
            let btn = document.querySelector(".del-btn")
            btn.classList.toggle("display-B")
        },
    },
    
    computed: {
        
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



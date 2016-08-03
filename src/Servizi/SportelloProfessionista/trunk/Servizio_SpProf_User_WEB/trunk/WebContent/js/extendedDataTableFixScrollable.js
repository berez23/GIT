ExtendedDataTable.DataTable.header.prototype.OnSepMouseMove = function(event) {
      if(this.dragColumnInfo && this.dragColumnInfo.mouseDown) {
           if(!this.dragColumnInfo.dragStarted) {
                this.dragColumnInfo.dragStarted = true;
                this._showSplitter(this.dragColumnInfo.srcElement.columnIndex);
           }
           var delta = Event.pointerX(event) - 
                this.dragColumnInfo.startX
           if (delta < this.minDelta) {
                delta = this.minDelta;
           }
           /*if (delta > this.maxDelta) {
                delta = this.maxDelta;
           }*/
           var x = this.dragColumnInfo.originalX + delta;
           var finalX = x - this.minColumnWidth - 6 //6 stands for sep span width;
           this.columnSplitter.moveToX(finalX);                     
           Event.stop(event);
      }
 }

 ExtendedDataTable.DataTable.header.prototype.OnSepMouseUp = function(event) {
      Event.stop(event);
      Event.stopObserving(document, 'mousemove', this.eventSepMouseMove);
      Event.stopObserving(document, 'mouseup', this.eventSepMouseUp);
      if(this.dragColumnInfo && this.dragColumnInfo.dragStarted) {
           this.dragColumnInfo.dragStarted = false;
           this.dragColumnInfo.mouseDown = false;

           var delta = Event.pointerX(event) - 
                this.dragColumnInfo.startX;
           if (delta < this.minDelta) {
                delta = this.minDelta;
           }
           /*if (delta > this.maxDelta) {
                delta = this.maxDelta;
           }*/
           var columnIndex = this.dragColumnInfo.srcElement.columnIndex;
           var newWidth = this.getColumnWidth(columnIndex) + delta;

           this.extDt.setColumnWidth(columnIndex, newWidth);
           this.setColumnWidth(columnIndex,newWidth);
           this.extDt.updateLayout();
           if (this.extDt.onColumnResize){
                //set properly value to this.columnWidths
                this.extDt.columnWidths = "";
                for (i=0; i<this.columnsNumber; i++){
                     this.extDt.columnWidths += "" + this.getColumnWidth(i) + ";";
                }//for
                this.extDt.onColumnResize(event, this.extDt.columnWidths);
           }
      }
      this._hideSplitter();
 }